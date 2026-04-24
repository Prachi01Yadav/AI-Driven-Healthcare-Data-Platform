# Arogya360 - Hospital Management System

Arogya360 is a comprehensive Hospital Management System built with Spring Boot and a modern, responsive Vanilla JS frontend. It is designed to streamline the management of patients, doctors, appointments, bills, and prescriptions.

## Features Added in the Latest Upgrade

### Backend & AI Capabilities
*   **Spring AI Integration:** Intelligent hospital assistant powered by OpenAI/Groq capable of answering hospital FAQs, standard medical queries, and checking doctor schedules.
*   **Smart Medicine Suggestions:** AI-driven one-click medicine and protocol suggestions based on patient disease records.
*   **Analytics Dashboard API:** A new `/dashboard/stats` endpoint aggregating vital data (total patients, doctors, appointments, and revenue) to provide a high-level overview.
*   **Enhanced Doctor Model:** Doctors now have configurable `workingDays` and `workingHours` to calculate availability.
*   **Enhanced Appointment Model:** Added an appointment `status` field (e.g., "SCHEDULED", "COMPLETED", "CANCELLED") for robust tracking.
*   **Search Functionality:** Introduced powerful search endpoints (`/patients/search?name=...` and `/doctors/search?name=...`) for quick filtering.

### Frontend
*   **Professional Medical Aesthetic:** Transitioned to a clean, modern white-and-blue theme (`#ffffff` background, `#2563eb` accents) using a crisp typography stack (Inter/Segoe UI) to ensure a trustworthy, clinical appearance.
*   **Dynamic Dashboard View:** A new default landing tab that visually displays high-level statistics and KPIs using responsive card layouts.
*   **Interactive UI/UX:** Integrated smooth micro-animations, hover effects, and modern form controls for an elevated user experience.

## Tech Stack
*   **Backend:** Java 21, Spring Boot (Data JPA, Web), MySQL Connector
*   **Frontend:** HTML5, CSS3 (Custom Variables, Flexbox/Grid), Vanilla JavaScript (ES6+ Fetch API)
*   **Deployment:** Docker, Render

## How to Run Locally

### Prerequisites
*   Java 21
*   Maven
*   MySQL Database (Running on localhost:3306)

### Setup
1.  **Configure Database:** Ensure MySQL is running. The application expects a database named `hospital_db` and credentials `root` / `Prachi0101_26` (which can be overridden via `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` environment variables).
2.  **Build and Run:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
3.  **Access the Application:** Open your browser and navigate to `http://localhost:8081`.

## Deployment (Render)
This project is configured for seamless deployment on **Render** using Docker.
1.  Sign up for a free account on [Render](https://render.com).
2.  Create a new **Web Service**.
3.  Connect your GitHub repository containing Arogya360.
4.  Render will automatically detect the `Dockerfile` and `render.yaml` configuration.
5.  In the Render dashboard, define the environment variables (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) pointing to your hosted MySQL instance (e.g., Aiven, PlanetScale, or a Render PostgreSQL database if migrating).
6.  Click **Deploy**.
