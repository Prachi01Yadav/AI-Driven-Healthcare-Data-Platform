package com.example.arogya360.service;

import com.example.arogya360.repository.AppointmentRepository;
import com.example.arogya360.repository.BillRepository;
import com.example.arogya360.repository.DoctorRepository;
import com.example.arogya360.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final BillRepository billRepository;

    public DashboardService(PatientRepository patientRepository,
                            DoctorRepository doctorRepository,
                            AppointmentRepository appointmentRepository,
                            BillRepository billRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPatients", patientRepository.count());
        stats.put("totalDoctors", doctorRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        stats.put("totalBills", billRepository.count());

        // Could also sum up the total revenue from bills
        double totalRevenue = billRepository.findAll().stream()
                .mapToDouble(bill -> bill.getAmount())
                .sum();
        stats.put("totalRevenue", totalRevenue);

        return stats;
    }
}
