package com.example.licensePlate.service;

import com.example.licensePlate.model.ParkingSession;
import com.example.licensePlate.repository.ParkingSessionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ParkingSessionService {

    @Autowired
    private ParkingSessionRepository parkingSessionRepository;


    public ParkingSession parkCar(String licensePlate) {
        try
        {
            ParkingSession session = new ParkingSession(licensePlate, LocalDateTime.now());
            return parkingSessionRepository.save(session);
        } catch (Exception e) {
            System.out.println("Unable to park car with license plate: " + licensePlate + ". Reason: " + e.getMessage());
            return null;
        }


    }

    @Transactional
    public boolean exitCar(String licensePlate) {
        System.out.println("Attempting to exit car with license plate: " + licensePlate);
        Optional<ParkingSession> session = parkingSessionRepository.findByLicensePlate(licensePlate);
        if (session.isPresent()) {
            parkingSessionRepository.deleteByLicensePlate(licensePlate);
            System.out.println("Car exited successfully: " + licensePlate);
            return true;
        } else {
            System.out.println("Car not found: " + licensePlate);
            return false;
        }
    }

    public List<ParkingSession> getAllParkedCars() {
        return parkingSessionRepository.findAll().stream().toList();
    }
}
