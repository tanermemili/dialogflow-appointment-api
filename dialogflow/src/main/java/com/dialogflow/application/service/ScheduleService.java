package com.dialogflow.application.service;

import com.dialogflow.application.helper.ParameterHelper;
import com.dialogflow.domain.entity.Schedule;
import com.dialogflow.infrastructure.repository.ScheduleRepository;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2WebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
                "dates, since we have a lot of capacity here:\n\n");

        for (Schedule schedule : schedules) {
            String scheduleString = extractScheduleString(schedule);

            responseText.append(schedule.getWeekday())
                    .append(": ")
                    .append("\n")
                    .append(scheduleString)
                    .append("\n");
        }

        responseText.append("which of these dates are feasible for you? Or do you prefer another day/time?");

        return responseText.toString();
    }

    public String findScheduleByWeekdayAndTime(GoogleCloudDialogflowV2WebhookRequest request) {
        StringBuilder responseText = new StringBuilder();

        Map<String, Object> parameters = ParameterHelper.getParametersFromRequest(request);

        String weekday = (String) parameters.get("weekday");
        String time = (String) parameters.get("timeslot");

        if (!weekday.isEmpty() && !time.isEmpty()) {
            return handleScheduleByWeekdayAndTime(responseText, weekday, time);
        } else if (!weekday.isEmpty()) {
            return handleScheduleByWeekday(responseText, weekday);
        } else if (!time.isEmpty()) {
            return handleScheduleByTime(responseText, time);
        }

        return "I'm sorry, but it looks like something went wrong when you tried to check the day/time.";
    }

    private String handleScheduleByWeekdayAndTime(StringBuilder responseText, String weekday, String time) {
        String timeString = getTimeString(time);

        if (timeString.isEmpty()) {
            responseText.append("I'm sorry, but it seems like you didn't provide a correct " +
                    "time slot. Please choose one of the (30min) time slots:\n" +
                    "08:00,\n08:30,\n09:00,\n15:00,\n15:30");
            return responseText.toString();
        }

        Optional<Schedule> schedule = scheduleRepository.findScheduleByWeekdayAndTimeslot(weekday, timeString);

        if (schedule.isPresent()) {
            responseText.append("The time slot on ")
                    .append(weekday)
                    .append(" at ")
                    .append(time)
                    .append(" is available! ")
                    .append("Do you want to make a booking?");
        }
        else {
            responseText.append("I'm very sorry, but it looks like that the time slot on ")
                    .append(weekday)
                    .append(" at ")
                    .append(time)
                    .append(" is already occupied.");
        }

        return responseText.toString();
    }

    private String handleScheduleByWeekday(StringBuilder responseText, String weekday) {
        Schedule schedule = scheduleRepository.findScheduleByWeekday(weekday);

        if (schedule.getEight() && schedule.getEightThirty() &&
                schedule.getNine() && schedule.getFifteen() &&
                schedule.getFifteenThirty()) {
            responseText
                    .append("I'm very sorry, but it looks like there are no free time slots available on ")
                    .append(schedule.getWeekday())
                    .append(".");
        } else {
            String scheduleString = extractScheduleString(schedule);

            responseText.append("Thanks for asking. These are the available slots for ")
                    .append(schedule.getWeekday())
                    .append(":\n")
                    .append(scheduleString)
                    .append("\ndo you want to make a booking?");
        }

        return responseText.toString();
    }

    private String handleScheduleByTime(StringBuilder responseText, String time) {
        String timeString = getTimeString(time);

        if (timeString.isEmpty()) {
            responseText.append("I'm sorry, but it seems like you didn't provide a correct " +
                    "time slot. Please choose one of the (30min) time slots: " +
                    "08:00, 08:30, 09:00, 15:00, 15:30.");
            return responseText.toString();
        }

        List<Schedule> schedules = scheduleRepository.findSchedulesByTimeslot(timeString);

        responseText.append("Thanks for asking. These are the days which have an open slot at ")
                .append(time)
                .append(":\n");

        for (Schedule schedule : schedules) {
            responseText.append(schedule.getWeekday())
                    .append(",\n");
        }

        responseText.append("\ndo you want to make a booking?");

        return responseText.toString();
    }

    private String getTimeString(String time) {
        return switch (time) {
            case "08:00" -> "eight";
            case "08:30" -> "eight_thirty";
            case "09:00" -> "nine";
            case "15:00" -> "fifteen";
            case "15:30" -> "fifteen_thirty";
            default -> "";
        };
    }

    private String extractScheduleString(Schedule schedule) {
        StringBuilder scheduleString = new StringBuilder();

        if (!schedule.getEight()) {
            scheduleString.append("08:00 - 08:30, \n");
        }
        if (!schedule.getEightThirty()) {
            scheduleString.append("08:30 - 09:00, \n");
        }
        if (!schedule.getNine()) {
            scheduleString.append("09:00 - 09:30, \n");
        }
        if (!schedule.getFifteen()) {
            scheduleString.append("15:00 - 15:30, \n");
        }
        if (!schedule.getFifteenThirty()) {
            scheduleString.append("15:30 - 16:00, \n");
        }

        return scheduleString.toString();
    }
}
