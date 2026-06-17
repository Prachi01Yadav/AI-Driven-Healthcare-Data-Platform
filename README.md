# Arogya 360 - AI-Driven Healthcare Management Platform

Arogya 360 is a comprehensive, AI-powered hospital management system designed to streamline patient records, appointments, digital prescriptions, emergency SOS routing, and blockchain-based audit trails.

## Core Features

- **Patient Management:** Track patient vitals, demographics, and medical history.
- **AI Health Risk Scoring:** Algorithmic calculation of patient health risk levels based on vitals, returning actionable recommendations.
- **AI Assistant:** Integration with Groq/Llama3 to provide real-time AI responses to hospital FAQs and basic medical protocol guidance.
- **E-Prescriptions:** Digital prescriptions with secure QR codes, cryptographic hashing, and dispensing workflows.
- **Blockchain Medical Records:** Tamper-proof, cryptographically secure (SHA-256) append-only ledger for all critical patient interactions.
- **Emergency SOS & Ambulance Dispatch:** GPS-based nearest ambulance routing using Haversine distance calculations.
- **Real-Time Notifications:** WebSocket (STOMP) integration for instant alerts.
- **Role-Based Access Control:** Secure JWT authentication for Admins, Doctors, and Patients.
- **Bilingual Interface:** Support for English and Hindi interfaces.

## Technology Stack

- **Backend:** Java, Spring Boot 3.5, Spring Security, Spring AI, WebSockets, JWT, ZXing (QR generation)
- **Database:** PostgreSQL
- **Frontend:** Vanilla HTML, CSS, JavaScript (Zero dependencies), Chart.js
- **Deployment:** Docker, Render

## Environment Setup

To run the application locally or in production, configure the following environment variables:

- \`DB_URL\`: PostgreSQL connection string (default: jdbc:postgresql://localhost:5432/hospital_db)
- \`DB_USERNAME\`: Database username (default: postgres)
- \`DB_PASSWORD\`: Database password (default: postgres)
- \`GROQ_API_KEY\`: Groq API key for the AI assistant (default: disabled)
- \`JWT_SECRET\`: A secure 256-bit base64 secret string for signing JWT tokens.
- \`PORT\`: Application port (default: 8081)

## Running Locally

1. Ensure PostgreSQL is running and a database named \`hospital_db\` exists.
2. Build the application: \`mvn clean package\`
3. Run the application: \`java -jar target/Arogya360-0.0.1-SNAPSHOT.jar\`
4. Access the web interface at \`http://localhost:8081\`

Note: On the first startup, a demo admin account will be automatically created:
- Username: admin
- Password: admin123

## Deployment

This project includes a \`Dockerfile\` and \`render.yaml\` optimized for deployment on Render.
Simply connect the repository to Render, provision a PostgreSQL database, and supply the environment variables listed above.

## API Endpoints Overview

- \`/api/auth/**\`: Registration, Login, Profile
- \`/patients/**\`: CRUD operations for Patients
- \`/doctors/**\`: CRUD operations for Doctors
- \`/appointments/**\`: Appointment scheduling and status updates
- \`/api/blockchain/**\`: Block addition and chain verification
- \`/api/health-risk/**\`: Health risk calculation
- \`/api/e-prescription/**\`: QR generation and verification
- \`/api/emergency/**\`: SOS dispatch and ambulance tracking
