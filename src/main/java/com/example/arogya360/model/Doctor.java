package com.example.arogya360.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "doctors")

public class Doctor {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String specialization;

    private String phone;
    private int experience;

    private String workingDays; // e.g. "Monday-Friday"
    private String workingHours; // e.g. "09:00-17:00"

    public Doctor() {}

    public Doctor(String name, String specialization,int experience, String phone, String workingDays, String workingHours) {
        this.name = name;
        this.specialization = specialization;
        this.experience=experience;
        this.phone = phone;
        this.workingDays = workingDays != null ? workingDays : "Mon-Fri";
        this.workingHours = workingHours != null ? workingHours : "09:00-17:00";
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWorkingDays() { return workingDays; }
    public void setWorkingDays(String workingDays) { this.workingDays = workingDays; }

    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
}
