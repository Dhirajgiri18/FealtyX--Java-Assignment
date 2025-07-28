package com.fealtyx.studentapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.fealtyx.studentapi.model.Student;

@Service
public class StudentService {
    private final Map<Integer, Student> students = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public Student createStudent(Student student) {
        int id = idCounter.getAndIncrement();
        student.setId(id);
        students.put(id, student);
        return student;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public Student getStudentById(int id) {
        return students.get(id);
    }

    public Student updateStudent(int id, Student updated) {
        Student existing = students.get(id);
        if (existing == null)
            return null;
        updated.setId(id);
        students.put(id, updated);
        return updated;
    }

    public boolean deleteStudent(int id) {
        return students.remove(id) != null;
    }
}
