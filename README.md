# 🎓 Student Management

> A production-style RESTful API application built with **Java** and **Spring Boot** for managing student records — featuring caching, security, and centralized exception handling.

---

## 📌 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Author](#author)

---

## Overview

The **Student Management System** is a backend REST API application that allows administrators to manage student data efficiently. It supports full CRUD operations, secured admin-only routes, Redis-powered caching for performance, and a centralized exception handling mechanism for clean, consistent error responses.

This project was built to demonstrate real-world Spring Boot backend development practices.

---

## Features

| Feature | Description |
|---|---|
| **CRUD Operations** | Create, Read, Update, Delete student records via REST APIs |
| **Global Exception Handler** | Centralized error handling using `@ControllerAdvice` for consistent API error responses |
| **Redis Caching** | Frequently accessed data cached with Redis to reduce DB load and improve response time |
| **Spring Interceptors** | Request-level interceptors to secure endpoints and restrict admin-only access |
| **MySQL Integration** | Persistent data storage with a normalized relational schema |
| **RESTful Design** | Follows REST principles with proper HTTP methods and status codes |

---

## Tech Stack

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Database | MySQL 8 |
| Caching | Redis |
| Build Tool | Maven |
| API Style | REST |

---

## Architecture

```
Client Request
      │
      ▼
 Interceptor Layer  ──── (Auth check / Admin access control)
      │
      ▼
  Controller Layer  ──── (REST endpoints)
      │
      ▼
  Service Layer     ──── (Business logic + Redis Cache)
      │
      ├──── Cache Hit?  ──── YES ──── Return cached response
      │
      ▼ NO
  Repository Layer  ──── (JPA / MySQL queries)
      │
      ▼
    MySQL DB
      │
      ▼
 Global Exception Handler ──── (Catches all errors, returns structured response)
```

---

## API Endpoints

### Student Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `GET` | `/api/students` | Get all students | User,Admin |
| `GET` | `/api/students/{id}` | Get student by ID | User,Admin |
| `POST` | `/api/students` | Create a new student | User,Admin |
| `PUT` | `/api/students/{id}` | Update student details | User,Admin |
| `DELETE` | `/api/students/{id}` | Delete a student | Admin |

### Sample Request — Create Student

```http
POST /api/students
Content-Type: application/json

{
  "name": "Shaik Junaid",
  "email": "junaid@example.com",
  "course": "Computer Science",
  "year": 2
}
```

### Sample Response

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "name": "Shaik Junaid",
    "email": "junaid@example.com",
    "course": "Computer Science",
    "year": 2
  }
}
```

### Error Response (via Global Exception Handler)

```json
{
  "status": "error",
  "message": "Student not found with id: 99",
  "timestamp": "2025-07-15T10:30:00"
}
```

---

## Getting Started

### Prerequisites

Make sure the following are installed on your system:

- Java 17+
- Maven 3.8+
- MySQL 8+
- Redis Server

### Installation

**1. Clone the repository**

```bash
git clone https://github.com/junaidaktkar/student-management-system.git
cd student-management-system
```

**2. Configure the database**

Create a MySQL database:

```sql
CREATE DATABASE student_db;
```

Update `src/main/resources/application.properties` with your credentials (see [Configuration](#configuration)).

**3. Start Redis server**

```bash
redis-server
```

**4. Build and run the application**

```bash
mvn clean install
mvn spring-boot:run
```

The API will be available at: `http://localhost:8080`

---

## Project Structure

```
student-management-system/
│
├── src/main/java/com/junaid/studentms/
│   ├── controller/
│   │   └── StudentController.java       # REST API endpoints
│   ├── service/
│   │   └── StudentService.java          # Business logic + Redis caching
│   ├── repository/
│   │   └── StudentRepository.java       # JPA repository (MySQL)
│   ├── model/
│   │   └── Student.java                 # Entity class
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java  # @ControllerAdvice handler
│   │   └── StudentNotFoundException.java
│   ├── interceptor/
│   │   └── AdminAccessInterceptor.java  # Admin access control
│   └── StudentManagementApplication.java
│
├── src/main/resources/
│   └── application.properties
│
├── pom.xml
└── README.md
```

## Key Implementation Highlights

### Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(StudentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("error", ex.getMessage(), LocalDateTime.now()));
    }
}
```

### Redis Caching
```java
@Cacheable(value = "students", key = "#id")
public Student getStudentById(Long id) {
    return studentRepository.findById(id)
            .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));
}
```

### Admin Interceptor
```java
@Component
public class AdminAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String role = request.getHeader("X-User-Role");
        if (!"ADMIN".equals(role)) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        return true;
    }
}
```

---

## Author

**Shaik Junaid Aktkar**

- GitHub: [@junaidaktkar](https://github.com/junaidaktkar)
- LinkedIn: [shaik-junaid-4bb485374](https://www.linkedin.com/in/shaik-junaid-4bb485374)
- Email: aktkarjunaid@gmail.com

---

> Built with focus on clean architecture, performance, and real-world backend best practices.
