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
    private final PatientService patientService;
    private final AppointmentService appointmentService;

    public DashboardService(PatientRepository patientRepository,
                            DoctorRepository doctorRepository,
                            AppointmentRepository appointmentRepository,
                            BillRepository billRepository,
                            PatientService patientService,
                            AppointmentService appointmentService) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.billRepository = billRepository;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPatients", patientRepository.count());
        stats.put("totalDoctors", doctorRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        stats.put("totalBills", billRepository.count());

        // Revenue
        double totalRevenue = billRepository.findAll().stream()
                .mapToDouble(bill -> bill.getAmount())
                .sum();
        stats.put("totalRevenue", totalRevenue);

        // Appointment status breakdown
        stats.put("scheduledAppointments", appointmentRepository.countByStatus("SCHEDULED"));
        stats.put("completedAppointments", appointmentRepository.countByStatus("COMPLETED"));
        stats.put("cancelledAppointments", appointmentRepository.countByStatus("CANCELLED"));

        // Bill payment breakdown
        stats.put("pendingBills", billRepository.countByPaymentStatus("PENDING"));
        stats.put("paidBills", billRepository.countByPaymentStatus("PAID"));

        // Recent data
        stats.put("recentPatients", patientService.getRecentPatients());
        stats.put("recentAppointments", appointmentService.getRecentAppointments());

        return stats;
    }
}
