package com.example.licensePlate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
public class Detection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detectionId;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Budapest")
    private LocalDateTime detectionDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String licensePlate;

    @Column(nullable = false)
    private double confidence;


    protected Detection() {

    }

    public Detection(LocalDateTime detectionDate, String status, String message, String licensePlate, double confidence) {
        this.detectionDate = detectionDate;
        this.status = status;
        this.message = message;
        this.licensePlate = licensePlate.toUpperCase();
        this.confidence = confidence;
    }


}
