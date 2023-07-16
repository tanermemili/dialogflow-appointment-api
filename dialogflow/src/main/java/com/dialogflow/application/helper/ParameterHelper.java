package com.dialogflow.application.helper;

import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2Context;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParameterHelper {
    public static Map<String, Object> getParametersFromRequest(GoogleCloudDialogflowV2WebhookRequest request) {
        List<GoogleCloudDialogflowV2Context> outputContext = request.getQueryResult().getOutputContexts();

        Optional<GoogleCloudDialogflowV2Context> desiredContext = outputContext.stream()
                .filter(context -> context.getName().contains("await_info"))
                .findFirst();

        if (desiredContext.isPresent()) {
            GoogleCloudDialogflowV2Context context = desiredContext.get();
            return context.getParameters();
        }

        return Collections.emptyMap();
    }
}
