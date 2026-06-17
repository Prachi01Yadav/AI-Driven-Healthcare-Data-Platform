package com.example.arogya360.controller;

import com.example.arogya360.model.Prescription;
import com.example.arogya360.service.EPrescriptionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/e-prescription")
public class EPrescriptionController {

    private final EPrescriptionService ePrescriptionService;

    public EPrescriptionController(EPrescriptionService ePrescriptionService) {
        this.ePrescriptionService = ePrescriptionService;
    }

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getQRCode(@PathVariable Long id) {
        return ePrescriptionService.generateQRCode(id);
    }

    @GetMapping("/verify/{hash}")
    public ResponseEntity<?> verifyPrescription(@PathVariable String hash) {
        try {
            Prescription p = ePrescriptionService.verifyPrescription(hash);
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}/dispense")
    public ResponseEntity<?> dispensePrescription(@PathVariable Long id) {
        try {
            Prescription p = ePrescriptionService.dispensePrescription(id);
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
