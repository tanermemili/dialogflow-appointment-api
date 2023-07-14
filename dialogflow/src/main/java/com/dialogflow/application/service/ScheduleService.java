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
        List<Schedule> schedules = scheduleRepository.findTopTwoSchedules();

        StringBuilder responseText = new StringBuilder("I would like to suggest you one of these " +
                "dates, since we have a lot of capacity here: ");

        for (Schedule schedule : schedules) {
            String eightString = "";
            String eightThirtyString = "";
            String nineString = "";
            String fifteenString = "";
            String fifteenThirtyString = "";

            if (!schedule.getEight()) {
                eightString = "08:00 - 08:30, ";
            }
            if (!schedule.getEightThirty()) {
                eightThirtyString = "08:30 - 09:00, ";
            }
            if (!schedule.getNine()) {
                nineString = "09:00 - 09:30, ";
            }
            if (!schedule.getFifteen()) {
                fifteenString = "15:00 - 15:30, ";
            }
            if (!schedule.getFifteenThirty()) {
                fifteenThirtyString = "15:30 - 16:00, ";
            }

            responseText.append(schedule.getWeekday() + ": " +
                    eightString + eightThirtyString + nineString + fifteenString + fifteenThirtyString);
        }

        responseText.append("which of these dates are feasible for you? Or do you prefer another day/time?");

        return responseText.toString();
    }

    public String UpdateScheduleByNewAppointment() {
        return "";
    }
}
