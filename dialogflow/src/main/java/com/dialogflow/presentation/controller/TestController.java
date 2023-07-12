package com.dialogflow.presentation.controller;

import com.dialogflow.application.service.TestService;
import com.dialogflow.domain.entity.Test;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    private final TestService testService;

    @Operation(summary = "Test the connection to the Tomcat Webservice")
    @GetMapping()
    ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("Successfully tested connection to Tomcat Webservice");
    }

    @Operation(summary = "Test the connection to the database")
    @GetMapping(path = {"/databaseconnection"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Test>> testDatabaseConnection() {
        List<Test> tests = testService.findTests();
        return ResponseEntity.ok(tests);
    }
}
