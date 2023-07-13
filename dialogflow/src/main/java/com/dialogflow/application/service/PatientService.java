package com.dialogflow.application.service;

import com.dialogflow.domain.entity.Patient;
import com.dialogflow.infrastructure.repository.PatientRepository;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2Context;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public String findPatient(GoogleCloudDialogflowV2WebhookRequest request) {
        List<GoogleCloudDialogflowV2Context> outputContext = request.getQueryResult().getOutputContexts();

        for (GoogleCloudDialogflowV2Context context : outputContext) {
            String contextName = context.getName();

            if (contextName.contains("await_info")) {
                Map<String, Object> parameters = context.getParameters();

                String numberString = (String)(parameters.get("number.original"));
                int insuranceNumber = Integer.parseInt(numberString);

                Optional<Patient> patient = patientRepository.findPatientByInsuranceNumber(insuranceNumber);

                if (!patient.isEmpty()) {
                    Patient patientData = patient.get();

                    return "It seems like you already have an appointment on " + patientData.getWeekday()
                            + " at " + patientData.getTime() + ".\n\nDo you want to reschedule your appointment or" +
                            " do you want to cancel it?";
                }
            }
        }

        return "placeholder";
    }
}
