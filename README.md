# dialogflow-appointment-api :date:

This is the REST API which provides the Webhook for Dialogflow in order to fulfill the users intents

## Technologies/Tool    :coffee:

- Java 17 LTS
- Postgres 15.1
- Gradle
- Spring Boot
- Spring Data JPA
- Liquibase
- Lombok
- Swagger
- Google API Services Dialogflow
- Ngrok (Provides a secure tunnel to your localhost)

## Getting Started :arrow_forward:

1. Import the zip file with the agent to your Dialogflow account
2. Install [Java 17](https://www.oracle.com/de/java/technologies/downloads/#java17) and [Postgres](https://www.postgresql.org/download/) on your local machine.
3. Set up your [Ngrok](https://ngrok.com/) account and follow the instructions from their website.
4. Use this command from the \dialogflow directory:

```./gradlew bootRun```

Press ```CTRL + C``` in order to stop the application.

5. Start the tunnel from Ngrok and forward it to the port 8080
6. Make sure that you copy the link provided by Ngrok to your Webhook in Dialogflow
7. Type ```webhook test``` in the Dialogflow Console to check the connection to your local webservice
