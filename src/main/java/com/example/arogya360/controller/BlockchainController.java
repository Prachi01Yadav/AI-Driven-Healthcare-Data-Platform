package com.example.arogya360.controller;

import com.example.arogya360.model.MedicalBlock;
import com.example.arogya360.service.BlockchainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping("/chain/{patientId}")
    public ResponseEntity<List<MedicalBlock>> getChain(@PathVariable Long patientId) {
        return ResponseEntity.ok(blockchainService.getChain(patientId));
    }

    @GetMapping("/verify/{patientId}")
    public ResponseEntity<Map<String, Object>> verifyChain(@PathVariable Long patientId) {
        boolean isValid = blockchainService.validateChain(patientId);
        long count = blockchainService.getBlockCount(patientId);
        return ResponseEntity.ok(Map.of(
                "valid", isValid,
                "blockCount", count
        ));
    }

    @PostMapping("/add")
    public ResponseEntity<MedicalBlock> addBlock(@RequestBody Map<String, String> request) {
        try {
            Long patientId = Long.parseLong(request.get("patientId"));
            String recordType = request.get("recordType");
            String dataPayload = request.get("dataPayload");
            return ResponseEntity.ok(blockchainService.addBlock(patientId, recordType, dataPayload));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
