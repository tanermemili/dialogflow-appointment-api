package com.dialogflow.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "patient")
@Data
public class Patient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String firstName;

    private String lastName;

    private Integer insuranceNumber;

    private String weekday;

    private String time;
}
