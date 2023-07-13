package com.dialogflow.presentation.controller;

import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2IntentMessage;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2IntentMessageText;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookRequest;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;

import static java.util.Arrays.asList;

@RestController
@RequiredArgsConstructor
public class DialogFlowWebhookController {

    private final JacksonFactory jacksonFactory;

    @PostMapping(value = "/dialogflow-webhook", produces = {MediaType.APPLICATION_JSON_VALUE})
    public String webhook(@RequestBody String rawData) throws IOException {
        //Step 1. Parse the request
        GoogleCloudDialogflowV2WebhookRequest request = jacksonFactory
                .createJsonParser(rawData)
                .parse(GoogleCloudDialogflowV2WebhookRequest.class);

        //Step 2. Process the request
        //Step 3. Build the response message
        GoogleCloudDialogflowV2IntentMessage msg = new GoogleCloudDialogflowV2IntentMessage();
        GoogleCloudDialogflowV2IntentMessageText text = new GoogleCloudDialogflowV2IntentMessageText();
        text.setText(Collections.singletonList("Welcome to Spring Boot von Taner"));
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
