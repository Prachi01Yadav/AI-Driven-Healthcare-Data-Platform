package com.example.arogya360.repository;

import com.example.arogya360.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findByDoctorId(Long doctorId);
    Optional<Prescription> findByVerificationHash(String verificationHash);
    List<Prescription> findByStatus(String status);
}
