package com.example.arogya360.service;

import com.example.arogya360.model.Patient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIAssistantService {

    private final ChatClient chatClient;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AIAssistantService(ChatClient.Builder chatClientBuilder, PatientService patientService, DoctorService doctorService) {
        this.chatClient = chatClientBuilder.defaultSystem("You are a helpful and knowledgeable Hospital Assistant for Arogya360. You help answer queries about standard medical protocols, hospital FAQs, and triage. Be concise and professional.").build();
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    public String chat(String query) {
        return chatClient.prompt().user(query).call().content();
    }

    public String suggestMedicine(Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) return "Patient not found.";
        
        String promptText = String.format("The patient %s (Age: %d) has the following disease/condition: %s. Suggest some common over-the-counter medicines or standard protocols. Note: Advise that this is an AI suggestion and a real doctor must be consulted.", 
            patient.getName(), patient.getAge(), patient.getDisease());
            
        return chatClient.prompt().user(promptText).call().content();
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
