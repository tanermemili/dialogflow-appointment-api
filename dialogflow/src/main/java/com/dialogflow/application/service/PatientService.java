package com.dialogflow.application.service;

import com.dialogflow.application.helper.ParameterHelper;
import com.dialogflow.domain.entity.Patient;
import com.dialogflow.domain.entity.Schedule;
import com.dialogflow.infrastructure.repository.PatientRepository;
import com.dialogflow.infrastructure.repository.ScheduleRepository;
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
    private final ScheduleRepository scheduleRepository;

    public List<Patient> findPatientsAsList() {
        return patientRepository.findAll();
    }

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
        Map<String, Object> parameters = ParameterHelper.getParametersFromRequest(request);

        String numberString = (String) (parameters.get("number.original"));
        int insuranceNumber = Integer.parseInt(numberString);

        Optional<Patient> patient = patientRepository.findPatientByInsuranceNumber(insuranceNumber);

        if (patient.isPresent()) {
            Patient patientData = patient.get();

            return "It seems like you already have an appointment on " + patientData.getWeekday()
                    + " at " + patientData.getTime() + ". Do you want to reschedule your appointment or" +
                    " do you want to cancel it?";
        }

        return "You don't have an appointment yet. Do you want to set one up?";
    }

    public String deletePatient(GoogleCloudDialogflowV2WebhookRequest request) {

        Map<String, Object> parameters = ParameterHelper.getParametersFromRequest(request);

        String numberString = (String) (parameters.get("number.original"));
        int insuranceNumber = Integer.parseInt(numberString);

        Optional<Patient> patient = patientRepository.findPatientByInsuranceNumber(insuranceNumber);

        if (patient.isPresent()) {
            patientRepository.deletePatientByInsuranceNumber(insuranceNumber);

            SetScheduleToFalseByWeekdayAndTime(patient.get().getWeekday(), patient.get().getTime());

            return "Your appointment on " + patient.get().getWeekday() + " at " + patient.get().getTime()
                    + " has been successfully canceled! Thank you for using our service. Have a nice day "
                    + patient.get().getFirstName() + "!";
        }


        return "I'm sorry, but something went wrong when trying to cancel your appointment.";
    }

    public String addPatient(GoogleCloudDialogflowV2WebhookRequest request) {
        Map<String, Object> parameters = ParameterHelper.getParametersFromRequest(request);

        String numberString = (String) (parameters.get("number.original"));
        int insuranceNumber = Integer.parseInt(numberString);

        String firstName = (String) parameters.get("person.original");
        String weekday = (String) parameters.get("weekday");
        String time = (String) parameters.get("timeslot");

        Optional<Patient> checkOccupiedSlot = patientRepository.findPatientByWeekdayAndTime(weekday, time);

        if (checkOccupiedSlot.isPresent()) {
            return "I'm very sorry, but the timeslot on " + weekday + " at " + time + " is already reserved.";
        }

        SetScheduleToTrueByWeekdayAndTime(weekday, time);

        Patient patient = new Patient(firstName, insuranceNumber, weekday, time);

        patientRepository.saveAndFlush(patient);

        return "You have successfully booked an appointment on " + patient.getWeekday() + " at " +
                patient.getTime() + " with Dr. Smith! Thank you for using our service. " +
                "Have a nice day!";
    }

    public Schedule SetScheduleToFalseByWeekdayAndTime(String weekday, String time) {
        Schedule schedule = scheduleRepository.findScheduleByWeekday(weekday);

        switch (time) {
            case "08:00" -> schedule.setEight(false);
            case "08:30" -> schedule.setEightThirty(false);
            case "09:00" -> schedule.setNine(false);
            case "15:00" -> schedule.setFifteen(false);
            case "15:30" -> schedule.setFifteenThirty(false);
        }

        scheduleRepository.saveAndFlush(schedule);

        return schedule;
    }

    private void SetScheduleToTrueByWeekdayAndTime(String weekday, String time) {
        Schedule schedule = scheduleRepository.findScheduleByWeekday(weekday);

        switch (time) {
            case "08:00" -> schedule.setEight(true);
            case "08:30" -> schedule.setEightThirty(true);
            case "09:00" -> schedule.setNine(true);
            case "15:00" -> schedule.setFifteen(true);
            case "15:30" -> schedule.setFifteenThirty(true);
        }

        scheduleRepository.saveAndFlush(schedule);
    }
}
