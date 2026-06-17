package com.example.arogya360.controller;

import com.example.arogya360.service.HealthRiskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/health-risk")
public class HealthRiskController {

    private final HealthRiskService healthRiskService;

    public HealthRiskController(HealthRiskService healthRiskService) {
        this.healthRiskService = healthRiskService;
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<?> getHealthRisk(@PathVariable Long patientId) {
        try {
            return ResponseEntity.ok(healthRiskService.calculateRiskScore(patientId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{patientId}/calculate")
    public ResponseEntity<?> calculateHealthRisk(@PathVariable Long patientId) {
        try {
            return ResponseEntity.ok(healthRiskService.calculateRiskScore(patientId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
