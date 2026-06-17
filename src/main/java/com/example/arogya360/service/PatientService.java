package com.example.arogya360.service;

import com.example.arogya360.model.Patient;
import com.example.arogya360.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public List<Patient> searchPatients(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient existing = patientRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setName(patientDetails.getName());
        existing.setAge(patientDetails.getAge());
        existing.setGender(patientDetails.getGender());
        existing.setDisease(patientDetails.getDisease());
        if (patientDetails.getPhone() != null) existing.setPhone(patientDetails.getPhone());
        if (patientDetails.getEmail() != null) existing.setEmail(patientDetails.getEmail());
        if (patientDetails.getAddress() != null) existing.setAddress(patientDetails.getAddress());
        if (patientDetails.getBloodPressure() != null) existing.setBloodPressure(patientDetails.getBloodPressure());
        if (patientDetails.getBloodSugar() > 0) existing.setBloodSugar(patientDetails.getBloodSugar());
        if (patientDetails.getBmi() > 0) existing.setBmi(patientDetails.getBmi());
        if (patientDetails.getSmokingStatus() != null) existing.setSmokingStatus(patientDetails.getSmokingStatus());
        if (patientDetails.getExerciseFrequency() != null) existing.setExerciseFrequency(patientDetails.getExerciseFrequency());
        if (patientDetails.getFamilyHistory() != null) existing.setFamilyHistory(patientDetails.getFamilyHistory());
        if (patientDetails.getAllergies() != null) existing.setAllergies(patientDetails.getAllergies());

        return patientRepository.save(existing);
    }

    public List<Patient> getRecentPatients() {
        List<Patient> all = patientRepository.findAll();
        int size = all.size();
        return all.subList(Math.max(0, size - 5), size);
    }
}
