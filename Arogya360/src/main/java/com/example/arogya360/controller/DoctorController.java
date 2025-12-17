package com.example.arogya360.controller;

import com.example.arogya360.model.Doctor;
import com.example.arogya360.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.saveDoctor(doctor));
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor != null) return ResponseEntity.ok(doctor);
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok("Doctor deleted successfully");
    }
    @PutMapping("/{id}")
public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctorDetails) {
    Doctor existingDoctor = doctorService.getDoctorById(id);
    if (existingDoctor == null) {
        return ResponseEntity.notFound().build();
    }

    // Update fields
    existingDoctor.setName(doctorDetails.getName());
    existingDoctor.setSpecialization(doctorDetails.getSpecialization());
    existingDoctor.setExperience(doctorDetails.getExperience());
    existingDoctor.setPhone(doctorDetails.getPhone());

    Doctor updatedDoctor = doctorService.saveDoctor(existingDoctor);
    return ResponseEntity.ok(updatedDoctor);
}


}
