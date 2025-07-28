package com.fealtyx.studentapi.controller;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fealtyx.studentapi.model.Student;
import com.fealtyx.studentapi.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentSummaryController {
    private static final Logger logger = LoggerFactory.getLogger(StudentSummaryController.class);
    @Autowired
    private StudentService studentService;

    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getStudentSummary(@PathVariable int id) {
        logger.info("Requesting summary for student ID: {}", id);
        Student student = studentService.getStudentById(id);
        if (student == null) {
            logger.warn("Student not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
        String summary = generateSummaryWithGemini(student);
        if (summary == null) {
            logger.error("Failed to generate summary for student ID: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate summary");
        }
        logger.info("Summary generated for student ID: {}", id);
        return ResponseEntity.ok(summary);
    }

    private String generateSummaryWithGemini(Student student) {
        logger.debug("Generating summary with Gemini for student: {}", student.getId());
        String prompt = String.format("Summarize the following student profile:\nName: %s\nAge: %d\nEmail: %s",
                student.getName(), student.getAge(), student.getEmail());
        try {
            // Replace YOUR_API_KEY with your actual Gemini API key
            String apiKey = "AIzaSyDbft-4oV2mOgR_ILI6lrMXrivY9xFFyTQ";
            String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
                    + apiKey;
            JSONObject requestBody = new JSONObject();
            requestBody.put("contents", new JSONObject[] {
                    new JSONObject().put("parts", new JSONObject[] {
                            new JSONObject().put("text", prompt)
                    })
            });

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString(), StandardCharsets.UTF_8))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            // Parse the summary from Gemini response
            if (json.has("candidates")) {
                JSONObject candidate = json.getJSONArray("candidates").getJSONObject(0);
                if (candidate.has("content")) {
                    JSONObject content = candidate.getJSONObject("content");
                    if (content.has("parts")) {
                        return content.getJSONArray("parts").getJSONObject(0).getString("text");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Gemini REST request failed: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Helper to extract 'response' field from Ollama JSON
    private String extractOllamaSummary(String json) {
        try {
            int responseIdx = json.indexOf("\"response\"");
            if (responseIdx == -1)
                return null;
            int colonIdx = json.indexOf(':', responseIdx);
            if (colonIdx == -1)
                return null;
            int quoteStart = json.indexOf('"', colonIdx + 1);
            int quoteEnd = json.indexOf('"', quoteStart + 1);
            if (quoteStart == -1 || quoteEnd == -1)
                return null;
            return json.substring(quoteStart + 1, quoteEnd).trim();
        } catch (Exception e) {
            return null;
        }
    }
}
