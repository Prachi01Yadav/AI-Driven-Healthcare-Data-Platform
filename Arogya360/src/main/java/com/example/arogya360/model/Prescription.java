package com.example.arogya360.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "Doctor is required")
    private Doctor doctor;

    @Column(nullable = false)
    @NotBlank(message = "Medicine name is required")
    private String medicineName;

    @Column(nullable = false)
    @NotBlank(message = "Dosage is required")
    private String dosage;

    @Column(nullable = false)
    @NotBlank(message = "Instructions are required")
    private String instructions;

    @Column(nullable = false)
    @NotNull(message = "Prescription date is required")
    private LocalDate date;

    // Constructors
    public Prescription() {}

    public Prescription(Patient patient, Doctor doctor, String medicineName, String dosage, String instructions, LocalDate date) {
        this.patient = patient;
        this.doctor = doctor;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.instructions = instructions;
        this.date = date;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
