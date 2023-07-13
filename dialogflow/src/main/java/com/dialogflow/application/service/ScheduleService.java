package com.dialogflow.application.service;

import com.dialogflow.domain.entity.Schedule;
import com.dialogflow.infrastructure.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<Schedule> findSchedulesAsList() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findTopTwoSchedulesAsList() {
        return scheduleRepository.findTopTwoSchedules();
    }

    public String findTopTwoSchedules() {
        String responseText = "";

        List<Schedule> schedules = scheduleRepository.findTopTwoSchedules();

        int freeScheduleSlots = 0;

        for (Schedule schedule : schedules) {

        }
        return "";
    }
}
