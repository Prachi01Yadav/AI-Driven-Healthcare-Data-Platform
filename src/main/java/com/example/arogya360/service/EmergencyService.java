package com.example.arogya360.service;

import com.example.arogya360.model.Ambulance;
import com.example.arogya360.model.EmergencyRequest;
import com.example.arogya360.repository.AmbulanceRepository;
import com.example.arogya360.repository.EmergencyRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmergencyService {

    private final EmergencyRequestRepository emergencyRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final NotificationService notificationService;

    public EmergencyService(EmergencyRequestRepository emergencyRepository,
                            AmbulanceRepository ambulanceRepository,
                            NotificationService notificationService) {
        this.emergencyRepository = emergencyRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.notificationService = notificationService;
    }

    public EmergencyRequest createSOSRequest(String patientName, String phone, double lat, double lng,
                                             String emergencyType, String notes) {
        EmergencyRequest request = new EmergencyRequest();
        request.setPatientName(patientName);
        request.setPatientPhone(phone);
        request.setLatitude(lat);
        request.setLongitude(lng);
        request.setEmergencyType(emergencyType);
        request.setNotes(notes);
        request.setCreatedAt(LocalDateTime.now());

        // Find nearest available ambulance
        List<Ambulance> available = ambulanceRepository.findByStatus("AVAILABLE");
        if (!available.isEmpty()) {
            Ambulance nearest = findNearestAmbulance(available, lat, lng);
            request.setAssignedAmbulanceId(nearest.getId());
            request.setStatus("DISPATCHED");

            // Calculate estimated arrival (rough: 2 min per km)
            double distance = haversineDistance(lat, lng, nearest.getLatitude(), nearest.getLongitude());
            int minutes = Math.max(3, (int) (distance * 2));
            request.setEstimatedArrival(minutes + " minutes");

            // Update ambulance status
            nearest.setStatus("ON_DUTY");
            nearest.setLastUpdated(LocalDateTime.now());
            ambulanceRepository.save(nearest);
        } else {
            request.setStatus("REQUESTED");
            request.setEstimatedArrival("Searching for available ambulance");
        }

        EmergencyRequest saved = emergencyRepository.save(request);

        // Send notification
        notificationService.createNotification(null, "EMERGENCY_ALERT",
                "Emergency SOS",
                "SOS request from " + patientName + " - Type: " + emergencyType);

        return saved;
    }

    public EmergencyRequest updateRequestStatus(Long id, String status) {
        EmergencyRequest request = emergencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emergency request not found"));
        request.setStatus(status);

        // If completed, free up the ambulance
        if ("COMPLETED".equals(status) && request.getAssignedAmbulanceId() != null) {
            ambulanceRepository.findById(request.getAssignedAmbulanceId()).ifPresent(amb -> {
                amb.setStatus("AVAILABLE");
                amb.setLastUpdated(LocalDateTime.now());
                ambulanceRepository.save(amb);
            });
        }

        return emergencyRepository.save(request);
    }

    public List<EmergencyRequest> getActiveEmergencies() {
        return emergencyRepository.findByStatusNot("COMPLETED");
    }

    public List<EmergencyRequest> getAllEmergencies() {
        return emergencyRepository.findTop10ByOrderByCreatedAtDesc();
    }

    public EmergencyRequest getEmergencyById(Long id) {
        return emergencyRepository.findById(id).orElse(null);
    }

    public List<Ambulance> getAllAmbulances() {
        return ambulanceRepository.findAll();
    }

    public Ambulance createAmbulance(Ambulance ambulance) {
        ambulance.setLastUpdated(LocalDateTime.now());
        return ambulanceRepository.save(ambulance);
    }

    public Ambulance updateAmbulanceLocation(Long id, double lat, double lng) {
        Ambulance ambulance = ambulanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ambulance not found"));
        ambulance.setLatitude(lat);
        ambulance.setLongitude(lng);
        ambulance.setLastUpdated(LocalDateTime.now());
        return ambulanceRepository.save(ambulance);
    }

    private Ambulance findNearestAmbulance(List<Ambulance> ambulances, double lat, double lng) {
        Ambulance nearest = ambulances.get(0);
        double minDistance = Double.MAX_VALUE;
        for (Ambulance amb : ambulances) {
            double distance = haversineDistance(lat, lng, amb.getLatitude(), amb.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                nearest = amb;
            }
        }
        return nearest;
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
