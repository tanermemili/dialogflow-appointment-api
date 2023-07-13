package com.dialogflow.presentation.controller;

import com.dialogflow.application.service.ScheduleService;
import com.dialogflow.domain.entity.Patient;
import com.dialogflow.domain.entity.Schedule;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "find all patients")
    @GetMapping("/all")
    public ResponseEntity<List<Schedule>> findPatients() {
        return ResponseEntity.ok(scheduleService.findSchedulesAsList());
    }
}
