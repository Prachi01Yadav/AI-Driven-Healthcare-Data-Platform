package com.example.arogya360.service;

import com.example.arogya360.model.Prescription;
import com.example.arogya360.repository.PrescriptionRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class EPrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public EPrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    public byte[] generateQRCode(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));

        String qrData = String.format(
                "AROGYA360-RX|ID:%d|Patient:%s|Doctor:%s|Medicine:%s|Dosage:%s|Date:%s|Hash:%s",
                prescription.getId(),
                prescription.getPatient() != null ? prescription.getPatient().getName() : "N/A",
                prescription.getDoctor() != null ? prescription.getDoctor().getName() : "N/A",
                prescription.getMedicineName(),
                prescription.getDosage(),
                prescription.getDate(),
                prescription.getVerificationHash() != null ? prescription.getVerificationHash() : generateVerificationHash(prescription)
        );

        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 300, 300);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    public String generateVerificationHash(Prescription prescription) {
        String data = prescription.getId() + "|" +
                (prescription.getPatient() != null ? prescription.getPatient().getId() : "") + "|" +
                (prescription.getDoctor() != null ? prescription.getDoctor().getId() : "") + "|" +
                prescription.getMedicineName() + "|" +
                prescription.getDate();
        String hash = sha256(data);

        // Save hash to prescription
        prescription.setVerificationHash(hash);
        prescription.setQrCodeData(data);
        prescription.setDigitalSignature("SIG-" + hash.substring(0, 16).toUpperCase());
        prescriptionRepository.save(prescription);

        return hash;
    }

    public Prescription verifyPrescription(String hash) {
        return prescriptionRepository.findByVerificationHash(hash)
                .orElseThrow(() -> new RuntimeException("Prescription not found or invalid hash"));
    }

    public Prescription dispensePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prescription not found"));
        prescription.setStatus("DISPENSED");
        return prescriptionRepository.save(prescription);
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
