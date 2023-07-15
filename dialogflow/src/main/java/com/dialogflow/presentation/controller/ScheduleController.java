package com.dialogflow.presentation.controller;

import com.dialogflow.application.service.ScheduleService;
import com.dialogflow.domain.entity.Schedule;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "find all patients")
    @GetMapping("/all")
    public ResponseEntity<List<Schedule>> findSchedulesAsList() {
        return ResponseEntity.ok(scheduleService.findSchedulesAsList());
    }

    @Operation(summary = "find the top two schedules with the most available capacity")
    @GetMapping("/toptwo")
    public ResponseEntity<List<Schedule>> findTopTwoSchedulesAsList() {
        return ResponseEntity.ok(scheduleService.findTopTwoSchedulesAsList());
    }

    @Operation(summary = "Check if the schedule by weekday and time is occupied")
    @PatchMapping()
    public ResponseEntity<Schedule> UpdateScheduleByWeekdayAndTime(@RequestParam String weekday,
                                                                   @RequestParam String time) {
        return ResponseEntity.ok(scheduleService.SetScheduleToFalseByWeekdayAndTime(weekday, time));
    }
}
