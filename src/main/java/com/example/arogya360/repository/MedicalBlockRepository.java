package com.example.arogya360.repository;

import com.example.arogya360.model.MedicalBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalBlockRepository extends JpaRepository<MedicalBlock, Long> {
    List<MedicalBlock> findByPatientIdOrderByBlockIndexAsc(Long patientId);
    Optional<MedicalBlock> findTopByPatientIdOrderByBlockIndexDesc(Long patientId);
    long countByPatientId(Long patientId);
}
