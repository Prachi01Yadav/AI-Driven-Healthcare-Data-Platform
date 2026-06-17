package com.example.arogya360.repository;

import com.example.arogya360.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByStatus(String status);
    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByPatientId(Long patientId);
    long countByStatus(String status);
    List<Appointment> findTop5ByOrderByAppointmentTimeDesc();
}
