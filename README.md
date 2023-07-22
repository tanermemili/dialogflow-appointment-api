# dialogflow-appointment-webhook :date:

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
- Docker

## Getting Started (locally) :arrow_forward:

1. Import the zip file with the agent to your Dialogflow account
2. Install [Java 17](https://www.oracle.com/de/java/technologies/downloads/#java17) and [Postgres](https://www.postgresql.org/download/) on your local machine.
3. Set up your [Ngrok](https://ngrok.com/) account and follow the instructions from their website.
4. Use these commands from the \dialogflow directory:

```$env:HOST = 'localhost'```
```./gradlew bootRun```

Press ```CTRL + C``` in order to stop the application.

5. Start the tunnel from Ngrok and forward it to the port 8080
6. Make sure that you copy the link provided by Ngrok to your Webhook in Dialogflow and add the ```/api/webhook``` suffix
7. Type ```webhook test``` in the Dialogflow Console to check the connection to your local webservice

## Start with Docker :whale:

The Container will connect with your locally installed Postgres-DB. If you don't have Postgres locally installed you can do so by pulling the postgres image ```docker pull postgres```

1. Go to the ```/dialogflow``` directory of the project where the Dockerfile is located and enter the following commands:

```docker build -t dialogflow:latest .```

```docker run -p 8080:8080 dialogflow:latest```

(Optional, in another shell):

```docker run -p 5432:5432 postgres```