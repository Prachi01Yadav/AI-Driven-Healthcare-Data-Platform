package com.example.arogya360.controller;

import com.example.arogya360.model.Ambulance;
import com.example.arogya360.model.EmergencyRequest;
import com.example.arogya360.service.EmergencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {

    private final EmergencyService emergencyService;

    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @PostMapping("/sos")
    public ResponseEntity<EmergencyRequest> createSOSRequest(@RequestBody Map<String, String> payload) {
        double lat = Double.parseDouble(payload.getOrDefault("latitude", "0"));
        double lng = Double.parseDouble(payload.getOrDefault("longitude", "0"));
        return ResponseEntity.ok(emergencyService.createSOSRequest(
                payload.get("patientName"),
                payload.get("patientPhone"),
                lat,
                lng,
                payload.get("emergencyType"),
                payload.get("notes")
        ));
    }

    @GetMapping("/active")
    public ResponseEntity<List<EmergencyRequest>> getActiveEmergencies() {
        return ResponseEntity.ok(emergencyService.getActiveEmergencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyRequest> getEmergencyById(@PathVariable Long id) {
        EmergencyRequest req = emergencyService.getEmergencyById(id);
        if (req != null) return ResponseEntity.ok(req);
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<EmergencyRequest> updateRequestStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(emergencyService.updateRequestStatus(id, body.get("status")));
    }

    @GetMapping("/ambulances")
    public ResponseEntity<List<Ambulance>> getAllAmbulances() {
        return ResponseEntity.ok(emergencyService.getAllAmbulances());
    }

    @PostMapping("/ambulances")
    public ResponseEntity<Ambulance> createAmbulance(@RequestBody Ambulance ambulance) {
        return ResponseEntity.ok(emergencyService.createAmbulance(ambulance));
    }

    @PutMapping("/ambulances/{id}/location")
    public ResponseEntity<Ambulance> updateAmbulanceLocation(@PathVariable Long id, @RequestBody Map<String, Double> payload) {
        return ResponseEntity.ok(emergencyService.updateAmbulanceLocation(id, payload.get("latitude"), payload.get("longitude")));
    }
}
