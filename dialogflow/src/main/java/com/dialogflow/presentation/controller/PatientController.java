package com.dialogflow.presentation.controller;

import com.dialogflow.application.service.PatientService;
import com.dialogflow.domain.entity.Patient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patient")
public class PatientController {
    private final PatientService patientService;


    @Operation(summary = "find all patients")
    @GetMapping("/all")
    public ResponseEntity<List<Patient>> findPatients() {
        return ResponseEntity.ok(patientService.findPatientsAsList());
    }
}
