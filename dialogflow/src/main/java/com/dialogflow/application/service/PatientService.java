package com.dialogflow.application.service;

import com.dialogflow.domain.entity.Patient;
import com.dialogflow.infrastructure.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {
    private final PatientRepository patientRepository;

    public String findPatients() {
        String responseText = "";

        List<Patient> patients = patientRepository.findAll();

        List<String> patientNames = patients.stream()
                .map(Patient::getFirstName)
                .collect(Collectors.toList());

        if (!patientNames.isEmpty()) {
            responseText = "The names of all patients are: " + String.join(", ", patientNames);
        } else {
            responseText = "There are no patients in the database.";
        }

        return responseText;
    }
}
