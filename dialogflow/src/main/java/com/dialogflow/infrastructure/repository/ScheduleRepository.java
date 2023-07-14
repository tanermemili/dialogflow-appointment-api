package com.dialogflow.infrastructure.repository;

import com.dialogflow.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query(value = "SELECT id, weekday, eight, eight_thirty, nine, fifteen, fifteen_thirty, " +
            "(CASE WHEN eight = FALSE THEN 1 ELSE 0 END + " +
            "CASE WHEN eight_thirty = FALSE THEN 1 ELSE 0 END + " +
            "CASE WHEN nine = FALSE THEN 1 ELSE 0 END + " +
            "CASE WHEN fifteen = FALSE THEN 1 ELSE 0 END + " +
            "CASE WHEN fifteen_thirty = FALSE THEN 1 ELSE 0 END) AS false_count " +
            "FROM public.schedule " +
            "ORDER BY false_count desc " +
            "LIMIT 2", nativeQuery = true)
    List<Schedule> findTopTwoSchedules();

    Schedule findScheduleByWeekday(String weekday);
}
