package com.example.arogya360.controller;

import com.example.arogya360.service.AIAssistantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIAssistantService aiService;

    public AIController(AIAssistantService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> request) {
        String query = request.get("query");
        if (query.toLowerCase().contains("schedule") || query.toLowerCase().contains("available") || query.toLowerCase().contains("off day")) {
            // Simple keyword router for demo purposes. In a real app, we'd use Spring AI Function Calling.
            // Extract a possible name (naive approach)
            String[] words = query.split(" ");
            String response = aiService.checkDoctorSchedule(words[words.length - 1]);
            return ResponseEntity.ok(Map.of("response", response));
        }
        String response = aiService.chat(query);
        return ResponseEntity.ok(Map.of("response", response));
    }

    @GetMapping("/suggest-medicine/{patientId}")
    public ResponseEntity<Map<String, String>> suggestMedicine(@PathVariable Long patientId) {
        String suggestion = aiService.suggestMedicine(patientId);
        return ResponseEntity.ok(Map.of("suggestion", suggestion));
    }
}
