package com.dialogflow.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity(name = "patient")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    private String firstName;

    private Integer insuranceNumber;

    private String weekday;

    private String time;

    public Patient(String firstName, int insuranceNumber, String weekday, String time) {
        this.firstName = firstName;
        this.insuranceNumber = insuranceNumber;
        this.weekday = weekday;
        this.time = time;
    }
}
