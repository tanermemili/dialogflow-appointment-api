package com.dialogflow.application.service;

import com.dialogflow.domain.entity.Patient;
import com.dialogflow.infrastructure.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {
    private final PatientRepository userRepository;

    public List<Patient> findPatients() {
        return userRepository.findAll();
    }
}
