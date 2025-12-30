# Arogya360

Arogya360 is a full‑stack healthcare management platform designed to simplify hospital operations and enhance patient–doctor interaction. It provides modules for managing patients, doctors, prescriptions, and appointments through a unified digital interface.

## Features

- Patient management (create, view, update, delete)
- Doctor management and profiles
- Appointment scheduling and calendar view
- Prescription creation, storage and retrieval
- Basic user authentication and role-based access (Admin / Doctor / Patient) — adjust as implemented
- Search and filters for patients/doctors/appointments
- Export / reporting placeholders


---

## Language composition

This repository is primarily implemented in:
- Java — 76.9%
- JavaScript — 14.0%
- CSS — 5.8%
- HTML — 3.3%


## Architecture & Tech Stack

A typical stack for this project (adapt to actual choices in the repo):

- Backend: Java (Spring Boot)
- Frontend: JavaScript + HTML + CSS
- Database: MySQL 
- Build tools: Maven npm 

## Prerequisites

- Java 11+ (or Java 17+) installed and JAVA_HOME configured
- Maven 
- A relational database (MySQL) or a configured embedded DB
- Git

---

### Backend (Java) — build & run

```bash
# build
mvn clean package

# run
mvn spring-boot:run
# or
java -jar target/<your-backend-artifact>.jar
```

### Database setup

1. Create a database (MySQL):
```sql
CREATE DATABASE arogya360;
CREATE USER 'arogya'@'localhost' IDENTIFIED BY 'change_me';
GRANT ALL PRIVILEGES ON arogya360.* TO 'arogya'@'localhost';
FLUSH PRIVILEGES;
```
---

## Configuration

Application configuration should be stored in environment variables or config files (examples):

- Spring-style properties (application.properties / application.yml)
  - spring.datasource.url=jdbc:mysql://localhost:3306/arogya360
  - spring.datasource.username=arogya
  - spring.datasource.password=change_me
  - server.port=8080

- Or use environment variables:
  - DATABASE_URL
  - DATABASE_USER
  - DATABASE_PASSWORD
  - JWT_SECRET (if JWT auth is used)

Provide a sample .env or application.properties.example in the repo for contributors.

---

## Testing

Run backend tests (Maven example):
```bash
mvn test
```

Run frontend tests (if present):
```bash
cd frontend
npm test
```





