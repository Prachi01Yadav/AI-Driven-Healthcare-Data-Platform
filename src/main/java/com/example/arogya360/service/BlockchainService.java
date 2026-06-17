package com.example.arogya360.service;

import com.example.arogya360.model.MedicalBlock;
import com.example.arogya360.repository.MedicalBlockRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlockchainService {

    private final MedicalBlockRepository blockRepository;

    public BlockchainService(MedicalBlockRepository blockRepository) {
        this.blockRepository = blockRepository;
    }

    public MedicalBlock addBlock(Long patientId, String recordType, String dataPayload) {
        MedicalBlock block = new MedicalBlock();
        block.setPatientId(patientId);
        block.setRecordType(recordType);
        block.setDataPayload(dataPayload);
        block.setTimestamp(LocalDateTime.now());

        // Get previous block
        Optional<MedicalBlock> lastBlock = blockRepository.findTopByPatientIdOrderByBlockIndexDesc(patientId);
        if (lastBlock.isPresent()) {
            block.setBlockIndex(lastBlock.get().getBlockIndex() + 1);
            block.setPreviousHash(lastBlock.get().getCurrentHash());
        } else {
            block.setBlockIndex(0);
            block.setPreviousHash("0");
        }

        // Compute data hash
        block.setDataHash(sha256(dataPayload));

        // Simple proof of work - find nonce where hash starts with "00"
        int nonce = 0;
        String hash;
        do {
            nonce++;
            hash = sha256(block.getBlockIndex() + block.getTimestamp().toString() +
                    block.getDataHash() + block.getPreviousHash() + nonce);
        } while (!hash.startsWith("00") && nonce < 100000);

        block.setNonce(nonce);
        block.setCurrentHash(hash);

        return blockRepository.save(block);
    }

    public boolean validateChain(Long patientId) {
        List<MedicalBlock> chain = blockRepository.findByPatientIdOrderByBlockIndexAsc(patientId);
        if (chain.isEmpty()) return true;

        for (int i = 1; i < chain.size(); i++) {
            MedicalBlock current = chain.get(i);
            MedicalBlock previous = chain.get(i - 1);

            // Check previous hash link
            if (!current.getPreviousHash().equals(previous.getCurrentHash())) {
                return false;
            }

            // Verify current hash is correctly computed
            String computedHash = sha256(current.getBlockIndex() + current.getTimestamp().toString() +
                    current.getDataHash() + current.getPreviousHash() + current.getNonce());
            if (!computedHash.equals(current.getCurrentHash())) {
                return false;
            }
        }
        return true;
    }

    public List<MedicalBlock> getChain(Long patientId) {
        return blockRepository.findByPatientIdOrderByBlockIndexAsc(patientId);
    }

    public long getBlockCount(Long patientId) {
        return blockRepository.countByPatientId(patientId);
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 hashing failed", e);
        }
    }
}
