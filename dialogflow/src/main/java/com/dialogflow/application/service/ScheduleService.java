package com.dialogflow.application.service;

import com.dialogflow.domain.entity.Patient;
import com.dialogflow.domain.entity.Schedule;
import com.dialogflow.infrastructure.repository.ScheduleRepository;
import com.google.api.services.dialogflow.v3.model.GoogleCloudDialogflowV2Context;
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

    public Schedule SetScheduleToFalseByWeekdayAndTime(String weekday, String time) {
        Schedule schedule = scheduleRepository.findScheduleByWeekday(weekday);

        switch (time) {
            case "08:00" -> schedule.setEight(false);
            case "08:30" -> schedule.setEightThirty(false);
            case "09:00" -> schedule.setNine(false);
            case "15:00" -> schedule.setFifteen(false);
            case "15:30" -> schedule.setFifteenThirty(false);
        }

        scheduleRepository.saveAndFlush(schedule);

        return schedule;
    }

    public Schedule SetScheduleToTrueByWeekdayAndTime(String weekday, String time) {
        Schedule schedule = scheduleRepository.findScheduleByWeekday(weekday);

        /*
        String timeString = switch (time) {
            case "08:00" -> {
                if (schedule.getEight()) {
                    yield "Sorry";
                }
                yield "ok";
            }
            case "08:30" -> "eight_thirty";
            case "09:00" -> "nine";
            case "15:00" -> "fifteen";
            case "15:30" -> "fifteen_thirty";
            default -> "ERROR could non determine you appointment!";
        };*/

        switch (time) {
            case "08:00" -> schedule.setEight(true);
            case "08:30" -> schedule.setEightThirty(true);
            case "09:00" -> schedule.setNine(true);
            case "15:00" -> schedule.setFifteen(true);
            case "15:30" -> schedule.setFifteenThirty(true);
        }

        scheduleRepository.saveAndFlush(schedule);

        return schedule;
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

    public String findScheduleByWeekdayAndTime(GoogleCloudDialogflowV2WebhookRequest request) {
        List<GoogleCloudDialogflowV2Context> outputContext = request.getQueryResult().getOutputContexts();

        StringBuilder responseText = new StringBuilder();

        for (GoogleCloudDialogflowV2Context context : outputContext) {
            String contextName = context.getName();

            if (contextName.contains("await_info")) {
                Map<String, Object> parameters = context.getParameters();

                String weekday = (String) parameters.get("weekday");
                String time = (String) parameters.get("timeslot");

                if (!weekday.isEmpty() && !time.isEmpty()) {
                    String timeString = switch (time) {
                        case "08:00" -> "eight";
                        case "08:30" -> "eight_thirty";
                        case "09:00" -> "nine";
                        case "15:00" -> "fifteen";
                        case "15:30" -> "fifteen_thirty";
                        default -> "00:00";
                    };

                    Optional<Schedule> schedule = scheduleRepository.findScheduleByWeekdayAndTimeslot(weekday, timeString);

                    if (!schedule.isEmpty()) {
                        responseText.append("The time slot on " + weekday + " at " + time + " is available! " +
                                            "Do you want to make a booking?");
                    }
                }
                else if (!weekday.isEmpty() && time.isEmpty()) {
                    Schedule schedule = scheduleRepository.findScheduleByWeekday(weekday);

                    String eightString = "";
                    String eightThirtyString = "";
                    String nineString = "";
                    String fifteenString = "";
                    String fifteenThirtyString = "";

                    if (schedule.getEight() && schedule.getEightThirty() &&
                            schedule.getNine() && schedule.getFifteen() &&
                            schedule.getFifteenThirty()) {
                        responseText.append("I'm very sorry, but it looks like there are no free " +
                                            "time slots available on " + schedule.getWeekday() +".");

                        break;
                    }

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

                    responseText.append("Thanks for asking. These are the available slots for " +
                                        schedule.getWeekday() + ": " + eightString + eightThirtyString + nineString +
                                        fifteenString + fifteenThirtyString + " do you want to make a booking?");
                }
                else if (weekday.isEmpty() && !time.isEmpty()) {

                    String timeString = switch (time) {
                        case "08:00" -> "eight";
                        case "08:30" -> "eight_thirty";
                        case "09:00" -> "nine";
                        case "15:00" -> "fifteen";
                        case "15:30" -> "fifteen_thirty";
                        default -> "";
                    };

                    if (timeString.isEmpty()) {
                        responseText.append("I'm sorry, but it seems like that you didn't provide a correct " +
                                            "time slot. Please choose one of the (30min) time slots: " +
                                            "08:00, 08:30, 09:00, 15:00, 15:30.");

                        return responseText.toString();
                    }

                    List<Schedule> schedules = scheduleRepository.findSchedulesByTimeslot(timeString);
                    responseText.append("Thanks for asking. These are the days which have an " +
                                        "open slot at " + time + ": ");

                    for (Schedule schedule : schedules) {
                        responseText.append(schedule.getWeekday() + ", ");
                    }

                    responseText.append(" do you want to make a booking?");
                }
            }
        }

        return responseText.toString();
    }
}
