package com.dialogflow.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "schedule")
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String weekday;

    private Boolean eight;

    private Boolean eightThirty;

    private Boolean nine;

    private Boolean fifteen;

    private Boolean fifteenThirty;
}
