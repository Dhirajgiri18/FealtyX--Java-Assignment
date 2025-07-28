

# Student API with Gemini AI Summary

<img width="1920" height="1080" alt="Screenshot 2025-07-28 215542" src="https://github.com/user-attachments/assets/18ee0dfe-eef0-49d3-88fd-f77b14724183" />


This is a Spring Boot REST API for managing student records with CRUD operations and AI-powered profile summaries using Google Gemini.

## Features
- Create, Read, Update, Delete (CRUD) endpoints for students
- In-memory storage (thread-safe)
- Input validation and error handling
- AI summary endpoint using Gemini REST API
- Swagger/OpenAPI documentation
- Logging

## Endpoints
| Method | URL                                 | Description                      |
|--------|-------------------------------------|----------------------------------|
| POST   | `/students`                         | Create a new student             |
| GET    | `/students`                         | Get all students                 |
| GET    | `/students/{id}`                    | Get student by ID                |
| PUT    | `/students/{id}`                    | Update student by ID             |
| DELETE | `/students/{id}`                    | Delete student by ID             |
| GET    | `/students/{id}/summary`            | Get AI summary for student       |

## How to Run
1. Make sure you have Java 21 and Maven installed.
2. Clone the repository:
   ```
   git clone https://github.com/yourusername/student-api-gemini.git
   cd student-api-gemini
   ```
3. Build and run:
   ```
   mvn spring-boot:run
   ```
4. The API will be available at `http://localhost:8081`.
5. Access Swagger UI for API docs:
   ```
   http://localhost:8081/swagger-ui/index.html
   ```

## Example Usage
### Create a Student
```
POST /students
Content-Type: application/json
{
  "name": "John Doe",
  "age": 20,
  "email": "john@example.com"
}
```

### Get AI Summary
```
GET /students/{id}/summary
```

## Gemini Integration
- The summary endpoint uses Google Gemini REST API.
- Set your Gemini API key in `StudentSummaryController.java`.

## Technologies Used
- Java 21
- Spring Boot
- Maven
- Lombok
- org.json
- Swagger/OpenAPI
- SLF4J Logging


## License
MIT
