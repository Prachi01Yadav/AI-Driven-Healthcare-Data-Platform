# Arogya 360 - AI-Driven Healthcare Management Platform

**Live Application:** [https://arogya360-071t.onrender.com](https://arogya360-071t.onrender.com)

Arogya 360 is a comprehensive, AI-powered hospital management system designed to streamline patient records, appointments, digital prescriptions, emergency SOS routing, and blockchain-based audit trails. Built with a robust Java Spring Boot backend and a modern vanilla frontend, it offers a secure, high-performance, and responsive healthcare solution.

---

## 1. System Architecture

The platform follows a classic client-server architecture with several advanced integrations for AI, real-time messaging, and cryptographic security.

```mermaid
graph TD
    subgraph Frontend
        UI[Vanilla HTML/CSS/JS]
        WS_Client[SockJS/STOMP]
    end

    subgraph Backend - Spring Boot
        Security[Spring Security & JWT Filter]
        Controllers[REST Controllers]
        Services[Business Logic Services]
        
        subgraph Core Modules
            Auth[Auth & User Mgmt]
            Vitals[Patient Vitals & History]
            Appt[Appointments]
            Billing[Bills & Payments]
        end

        subgraph Advanced Modules
            Blockchain[Blockchain Audit Service]
            AI[Groq AI Assistant Service]
            EPres[E-Prescription & ZXing]
            Emergency[Emergency Haversine Dispatch]
            Notif[WebSocket Notification Service]
        end
    end

    subgraph External Services
        Groq[Groq Llama-3 API]
    end

    subgraph Database
        Postgres[(PostgreSQL)]
    end

    UI -->|HTTP REST| Security
    WS_Client <-->|WebSockets| Notif
    Security --> Controllers
    Controllers --> Services
    Services --> Core Modules
    Services --> Advanced Modules
    
    AI -->|LLM Prompts| Groq
    Advanced Modules --> Postgres
    Core Modules --> Postgres
```

---

## 2. Core Features

### AI-Powered Health Analytics
- **Algorithmic Risk Scoring:** Calculates a real-time risk score (0-100) based on patient vitals (Blood Pressure, BMI, Sugar levels, Smoking/Exercise habits, and Family History).
- **AI Medical Assistant:** An integrated chatbot powered by Groq's Llama-3 model. It can answer hospital FAQs, guide patients through basic medical protocols, and analyze general symptoms.

### Security & Cryptography
- **Blockchain Medical Records:** Implements a tamper-proof, append-only ledger using SHA-256 hashing. Every prescription, diagnosis, and bill is added as a cryptographically linked block, ensuring data integrity.
- **Role-Based Access Control (RBAC):** Secure JWT authentication isolating functionality for Admins, Doctors, and Patients.

### Emergency & Dispatch
- **GPS SOS Routing:** An Emergency SOS module that uses the Haversine distance formula to instantly locate and dispatch the nearest available ambulance to a patient's latitude/longitude.

### Digital Workflow
- **E-Prescriptions:** Generates digital prescriptions embedded with ZXing QR codes for instant pharmacy verification.
- **Real-Time Notifications:** Uses STOMP over WebSockets to deliver instant alerts (e.g., "Ambulance Dispatched" or "Appointment Scheduled") without page refreshes.

---

## 3. Workflows by Role

### The Admin Role
The Admin has unrestricted oversight of the hospital's operations.
- **Dashboard:** Views high-level analytics, total revenue, patient counts, and active emergencies.
- **Staff Management:** Registers new doctors, assigns specializations, and manages their schedules.
- **Emergency Oversight:** Monitors active SOS requests and tracks ambulance fleet locations on a global grid.
- **Audit:** Has the authority to verify the Blockchain audit chain to ensure no medical records have been tampered with.

### The Doctor Role
Doctors are focused on patient care and scheduling.
- **Patient Management:** Accesses assigned patients, updates their vitals, and triggers the AI Health Risk Assessment to determine critical care needs.
- **Appointments:** Views their daily schedule, marks appointments as COMPLETED or CANCELLED, and reviews patient history prior to consultations.
- **E-Prescriptions:** Issues digital prescriptions. The system automatically signs these and generates a scannable QR code for the patient.

### The Patient Role
Patients have a read-only or request-driven interface focused on their personal care.
- **Self-Service:** Books appointments with available doctors based on specialization.
- **Medical Records:** Views their own risk scores, past appointments, and billing history.
- **Pharmacy & Verification:** Downloads QR-coded E-Prescriptions to present at pharmacies.
- **Emergency:** One-click SOS button that instantly beams their GPS location to the hospital's dispatch center.
- **AI Triage:** Chats with the AI Assistant to ask preliminary questions before booking an appointment.

---

## 4. Technology Stack

- **Backend:** Java 21, Spring Boot 3.3.x, Spring Security, Spring Data JPA, Spring AI
- **Database:** PostgreSQL (Cloud-hosted via Render)
- **Frontend:** Vanilla HTML5, CSS3, JavaScript (Zero heavyweight dependencies)
- **Libraries:** jjwt (Authentication), ZXing (QR Codes), Chart.js (Analytics), SockJS/STOMP (WebSockets)
- **Hosting:** Render (Web Service & Managed PostgreSQL Database)

---

## 5. Local Development Setup

If you wish to clone and run this repository locally:

1. Clone the repository.
2. Ensure you have Java 21 and Maven installed.
3. Configure your local `application.properties` or set the following environment variables:
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `GROQ_API_KEY`
   - `JWT_SECRET`
4. Run the application:
   ```bash
   mvn clean spring-boot:run
   ```
5. Navigate to `http://localhost:8081` in your browser.
