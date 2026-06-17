package com.example.arogya360.service;

import com.example.arogya360.model.Appointment;
import com.example.arogya360.repository.AppointmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment saveAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public Appointment updateAppointmentStatus(Long id, String status) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        if (appointment == null) return null;
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getRecentAppointments() {
        return appointmentRepository.findTop5ByOrderByAppointmentTimeDesc();
    }

    public long countByStatus(String status) {
        return appointmentRepository.countByStatus(status);
    }
}
