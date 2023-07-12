package com.dialogflow;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Dialogflow fulfillment webservice",
                version = "1.0.0",
                description = "This is the webservice which provides the necessary endpoints to fulfill the users intents"
        )
)
public class DialogflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(DialogflowApplication.class, args);
    }

}
