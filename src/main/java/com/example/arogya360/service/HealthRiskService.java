package com.example.arogya360.service;

import com.example.arogya360.model.Patient;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HealthRiskService {

    private final PatientService patientService;

    public HealthRiskService(PatientService patientService) {
        this.patientService = patientService;
    }

    public Map<String, Object> calculateRiskScore(Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        if (patient == null) {
            throw new RuntimeException("Patient not found");
        }

        int score = 10; // base score
        List<Map<String, String>> factors = new ArrayList<>();

        // Age factor
        if (patient.getAge() > 60) {
            score += 15;
            factors.add(createFactor("Advanced Age", "+15", "Regular health screenings recommended every 6 months"));
        } else if (patient.getAge() > 45) {
            score += 8;
            factors.add(createFactor("Middle Age", "+8", "Annual comprehensive health checkup recommended"));
        } else if (patient.getAge() > 30) {
            score += 3;
            factors.add(createFactor("Age Factor", "+3", "Maintain regular exercise and balanced diet"));
        }

        // BMI factor
        if (patient.getBmi() > 0) {
            if (patient.getBmi() > 30) {
                score += 15;
                factors.add(createFactor("Obesity (BMI > 30)", "+15", "Consult a nutritionist. Aim for gradual weight loss through diet and exercise"));
            } else if (patient.getBmi() > 25) {
                score += 8;
                factors.add(createFactor("Overweight (BMI > 25)", "+8", "Increase physical activity. Monitor caloric intake"));
            } else if (patient.getBmi() < 18.5) {
                score += 5;
                factors.add(createFactor("Underweight (BMI < 18.5)", "+5", "Increase nutrient-rich food intake. Consult a dietitian"));
            }
        }

        // Blood pressure factor
        if (patient.getBloodPressure() != null && !patient.getBloodPressure().isEmpty()) {
            try {
                String[] parts = patient.getBloodPressure().split("/");
                int systolic = Integer.parseInt(parts[0].trim());
                if (systolic > 140) {
                    score += 15;
                    factors.add(createFactor("High Blood Pressure (>140)", "+15", "Immediate medical attention. Reduce sodium intake. Regular BP monitoring"));
                } else if (systolic > 130) {
                    score += 8;
                    factors.add(createFactor("Elevated Blood Pressure (>130)", "+8", "Lifestyle modifications. Reduce stress. Limit salt and alcohol"));
                }
            } catch (Exception ignored) {}
        }

        // Blood sugar factor
        if (patient.getBloodSugar() > 0) {
            if (patient.getBloodSugar() > 200) {
                score += 15;
                factors.add(createFactor("Diabetic Range (>200 mg/dL)", "+15", "Urgent: Consult endocrinologist. Strict blood sugar management needed"));
            } else if (patient.getBloodSugar() > 140) {
                score += 10;
                factors.add(createFactor("Pre-Diabetic Range (>140 mg/dL)", "+10", "Dietary changes required. Regular glucose monitoring. Increase fiber intake"));
            } else if (patient.getBloodSugar() > 100) {
                score += 5;
                factors.add(createFactor("Borderline Blood Sugar (>100 mg/dL)", "+5", "Monitor diet. Reduce refined sugars. Regular exercise recommended"));
            }
        }

        // Smoking factor
        if ("CURRENT".equalsIgnoreCase(patient.getSmokingStatus())) {
            score += 15;
            factors.add(createFactor("Current Smoker", "+15", "Strongly advised to quit smoking. Consider nicotine replacement therapy"));
        } else if ("FORMER".equalsIgnoreCase(patient.getSmokingStatus())) {
            score += 5;
            factors.add(createFactor("Former Smoker", "+5", "Good progress. Continue smoke-free lifestyle. Regular lung screenings"));
        }

        // Exercise factor
        if ("NONE".equalsIgnoreCase(patient.getExerciseFrequency())) {
            score += 10;
            factors.add(createFactor("No Exercise", "+10", "Start with 30 mins of walking daily. Gradually increase intensity"));
        } else if ("LIGHT".equalsIgnoreCase(patient.getExerciseFrequency())) {
            score += 5;
            factors.add(createFactor("Light Exercise Only", "+5", "Increase to moderate activity. Aim for 150 mins per week"));
        }

        // Family history
        if (patient.getFamilyHistory() != null && !patient.getFamilyHistory().isEmpty()) {
            score += 8;
            factors.add(createFactor("Family History Present", "+8", "Genetic predisposition detected. Proactive screening recommended"));
        }

        // Disease factor
        if (patient.getDisease() != null) {
            String disease = patient.getDisease().toLowerCase();
            if (disease.contains("diabetes")) {
                score += 10;
                factors.add(createFactor("Diagnosed: Diabetes", "+10", "Strict glycemic control. Regular HbA1c testing"));
            }
            if (disease.contains("heart") || disease.contains("cardiac")) {
                score += 10;
                factors.add(createFactor("Diagnosed: Heart Condition", "+10", "Cardiac rehabilitation. Regular ECG and stress tests"));
            }
            if (disease.contains("cancer")) {
                score += 10;
                factors.add(createFactor("Diagnosed: Cancer", "+10", "Follow oncologist treatment plan. Regular imaging and blood work"));
            }
            if (disease.contains("stroke")) {
                score += 10;
                factors.add(createFactor("Diagnosed: Stroke History", "+10", "Blood thinners as prescribed. Monitor BP closely. Speech/physical therapy"));
            }
        }

        // Cap at 100
        score = Math.min(score, 100);

        // Determine risk level
        String riskLevel;
        if (score >= 75) riskLevel = "CRITICAL";
        else if (score >= 50) riskLevel = "HIGH";
        else if (score >= 30) riskLevel = "MODERATE";
        else riskLevel = "LOW";

        // Save score to patient
        patient.setRiskScore(score);
        patientService.savePatient(patient);

        Map<String, Object> result = new HashMap<>();
        result.put("patientId", patientId);
        result.put("patientName", patient.getName());
        result.put("riskScore", score);
        result.put("riskLevel", riskLevel);
        result.put("factors", factors);
        return result;
    }

    private Map<String, String> createFactor(String name, String impact, String recommendation) {
        Map<String, String> factor = new HashMap<>();
        factor.put("name", name);
        factor.put("impact", impact);
        factor.put("recommendation", recommendation);
        return factor;
    }
}
