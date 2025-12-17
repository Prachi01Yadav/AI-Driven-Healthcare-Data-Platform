package com.example.arogya360.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.arogya360.model.Patient;
import org.springframework.stereotype.Repository;

@Repository

public interface PatientRepository extends JpaRepository<Patient , Long> {

    // Basic CRUD operations are automatically available
}
