# Stage 1: Build the application
FROM eclipse-temurin:20.0.1_9-jdk-jammy AS builder

WORKDIR /app

COPY ./ ./

# Download Gradle distribution and dependencies
RUN ./gradlew --no-daemon dependencies
RUN ./gradlew --no-daemon build

# Stage 2: Create final image with JRE and application JAR
FROM eclipse-temurin:20.0.1_9-jre-alpine

ENV HOST=host.docker.internal

WORKDIR /app

COPY --from=builder app/build/libs/dialogflow-0.0.1-SNAPSHOT.jar /app/dialogflow-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "dialogflow-0.0.1-SNAPSHOT.jar"]