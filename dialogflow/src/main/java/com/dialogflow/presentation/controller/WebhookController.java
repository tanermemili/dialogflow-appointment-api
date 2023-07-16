package com.dialogflow.presentation.controller;

import com.dialogflow.application.service.PatientService;
import com.dialogflow.application.service.ScheduleService;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v3.model.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WebhookController {

    private final JacksonFactory jacksonFactory;
    private final PatientService patientService;
    private final ScheduleService scheduleService;

    @Operation(summary = "The webhook for dialogflow")
    @PostMapping(value = "/webhook", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String webhook(@RequestBody String rawData) throws IOException {
        //Step 1. Parse the request
        GoogleCloudDialogflowV2WebhookRequest request = jacksonFactory
                .createJsonParser(rawData)
                .parse(GoogleCloudDialogflowV2WebhookRequest.class);

        //Step 2. Determine the intent of the request
        String intentName = request.getQueryResult().getIntent().getDisplayName();
        String responseText;

        switch (intentName) {
            case "getAllPatients" -> responseText = patientService.findPatients();
            case "NameAndInsuranceYes" -> responseText = patientService.findPatient(request);
            case "NameAndInsuranceYesCancel" -> responseText = patientService.deletePatient(request);
            case "NameAndInsuranceYesSchedule" -> responseText = scheduleService.findTopTwoSchedules();
            case "NameAndInsuranceYesScheduleBooking" -> responseText = patientService.addPatient(request);
            case "NameAndInsuranceYesScheduleAskDayAndTime" -> responseText = scheduleService.findScheduleByWeekdayAndTime(request);
            case "WebhookTest" -> responseText = "Successfully tested connection to your Webhook!";
            default -> responseText = "I'm sorry, I don't understand your request.";
        }

        //Step 3. Build the response message
        GoogleCloudDialogflowV2IntentMessage msg = new GoogleCloudDialogflowV2IntentMessage();
        GoogleCloudDialogflowV2IntentMessageText text = new GoogleCloudDialogflowV2IntentMessageText();
        text.setText(Collections.singletonList(responseText));
        msg.setText(text);

        GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse();
        response.setFulfillmentMessages(List.of(msg));

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jacksonFactory.createJsonGenerator(stringWriter);
        jsonGenerator.enablePrettyPrint();
        jsonGenerator.serialize(response);
        jsonGenerator.flush();

        return stringWriter.toString();
    }
}
