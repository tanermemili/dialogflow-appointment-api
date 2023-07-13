package com.dialogflow.presentation.controller;

import com.dialogflow.application.service.PatientService;
import com.dialogflow.domain.entity.Patient;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2IntentMessage;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2IntentMessageText;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookResponse;
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
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WebhookController {

    private final JacksonFactory jacksonFactory;

    private final PatientService patientService;

    @Operation(summary = "The webhook for dialogflow")
    @PostMapping(value = "/webhook", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String webhook(@RequestBody String rawData) throws IOException {
        //Step 1. Parse the request
        GoogleCloudDialogflowV2WebhookRequest request = jacksonFactory
                .createJsonParser(rawData)
                .parse(GoogleCloudDialogflowV2WebhookRequest.class);

        //Step 2. Process the request
        String intentName = request.getQueryResult().getIntent().getDisplayName();
        String responseText;

        if (intentName.equals("getAllPatients")) {
            // Retrieve all patients from the repository
            List<Patient> patients = patientService.findPatients();

            // Extract the names of the patients
            List<String> patientNames = patients.stream()
                    .map(Patient::getFirstName)
                    .collect(Collectors.toList());

            if (!patientNames.isEmpty()) {
                responseText = "The names of all patients are: " + String.join(", ", patientNames);
            } else {
                responseText = "There are no patients in the database.";
            }
        } else {
            // Handle other intents or unknown intents
            responseText = "I'm sorry, I don't understand your request.";
        }

        //Step 3. Build the response message
        GoogleCloudDialogflowV2IntentMessage msg = new GoogleCloudDialogflowV2IntentMessage();
        GoogleCloudDialogflowV2IntentMessageText text = new GoogleCloudDialogflowV2IntentMessageText();
        text.setText(Collections.singletonList(responseText));
        msg.setText(text);

        GoogleCloudDialogflowV2WebhookResponse response = new GoogleCloudDialogflowV2WebhookResponse();
        response.setFulfillmentMessages(asList(msg));

        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jacksonFactory.createJsonGenerator(stringWriter);
        jsonGenerator.enablePrettyPrint();
        jsonGenerator.serialize(response);
        jsonGenerator.flush();

        return stringWriter.toString();
    }
}
