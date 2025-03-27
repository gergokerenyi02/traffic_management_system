package com.example.licensePlate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "parking_sessions")
public class ParkingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    protected ParkingSession() {

    }

    public ParkingSession(String licensePlate, LocalDateTime entryTime) {
        this.licensePlate = licensePlate;
        this.entryTime = entryTime;
    }

}
