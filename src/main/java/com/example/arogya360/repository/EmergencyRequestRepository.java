package com.example.arogya360.repository;

import com.example.arogya360.model.EmergencyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequest, Long> {
    List<EmergencyRequest> findByStatus(String status);
    List<EmergencyRequest> findTop10ByOrderByCreatedAtDesc();
    List<EmergencyRequest> findByStatusNot(String status);
}
