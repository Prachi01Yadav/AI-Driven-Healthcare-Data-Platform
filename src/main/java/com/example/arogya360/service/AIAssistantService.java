package com.example.arogya360.service;

import com.example.arogya360.model.Patient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIAssistantService {

    private ChatClient chatClient;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private boolean aiAvailable = false;

    public AIAssistantService(ChatClient.Builder chatClientBuilder, PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        try {
            this.chatClient = chatClientBuilder.defaultSystem("You are a helpful and knowledgeable Hospital Assistant for Arogya360. You help answer queries about standard medical protocols, hospital FAQs, and triage. Be concise and professional.").build();
            this.aiAvailable = true;
        } catch (Exception e) {
            this.aiAvailable = false;
            System.out.println("AI Assistant not available: " + e.getMessage());
        }
    }

    public String chat(String query) {
        if (!aiAvailable) {
            return "AI features are not configured. Please set the GROQ_API_KEY environment variable to enable the AI assistant.";
        }
        try {
            return chatClient.prompt().user(query).call().content();
        } catch (Exception e) {
            return "AI service is temporarily unavailable. Please try again later. Error: " + e.getMessage();
        }
    }

    public String suggestMedicine(Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) return "Patient not found.";

        if (!aiAvailable) {
            return "AI features are not configured. Please set the GROQ_API_KEY environment variable to enable medicine suggestions.";
        }

        try {
            String promptText = String.format("The patient %s (Age: %d) has the following disease/condition: %s. Suggest some common over-the-counter medicines or standard protocols. Note: Advise that this is an AI suggestion and a real doctor must be consulted.",
                patient.getName(), patient.getAge(), patient.getDisease());
            return chatClient.prompt().user(promptText).call().content();
        } catch (Exception e) {
            return "AI service is temporarily unavailable. Please try again later.";
        }
    }

    public String checkDoctorSchedule(String doctorName) {
        var doctors = doctorService.searchDoctors(doctorName);
        if (doctors.isEmpty()) {
            return "No doctor found with that name.";
        }
        var doctor = doctors.get(0);
        return String.format("Dr. %s (%s) is available on %s from %s.",
            doctor.getName(), doctor.getSpecialization(),
            doctor.getWorkingDays() != null ? doctor.getWorkingDays() : "Mon-Fri",
            doctor.getWorkingHours() != null ? doctor.getWorkingHours() : "09:00-17:00");
    }
}
