package com.example.licensePlate.controller;

import com.example.licensePlate.model.ParkingSession;
import com.example.licensePlate.service.ParkingSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parking")
public class ParkingSessionController {

    @Autowired
    private ParkingSessionService parkingSessionService;



    @PostMapping("/enter/{licensePlate}")
    public ResponseEntity<?> enterCar(@PathVariable String licensePlate) {
        ParkingSession saved = parkingSessionService.parkCar(licensePlate);
        Map<Object, String> response = new HashMap<>();
        if (saved == null) {
            response.put("status", "error");
            response.put("licensePlate", licensePlate);
            response.put("message", "Car with license plate " + licensePlate + " is already parked!");
        } else
        {
            response.put("status", "success");
            response.put("licensePlate", licensePlate);
            response.put("message", "Car with license plate " + licensePlate + " parked successfully!");

            System.out.println("Car with license plate " + licensePlate + " parked successfully!");
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/exit/{licensePlate}")
    public ResponseEntity<?> exitCar(@PathVariable String licensePlate) {
        Map<Object, String> response = new HashMap<>();
        boolean success = parkingSessionService.exitCar(licensePlate);
        if (success) {
            response.put("status", "success");
            response.put("licensePlate", licensePlate);
            response.put("message", "Car with license plate " + licensePlate + " exited successfully!");
        } else {
            response.put("status", "error");
            response.put("licensePlate", licensePlate);
            response.put("message", "Car with license plate " + licensePlate + " not exited.");
        }
        return ResponseEntity.ok(response);
    }



    @GetMapping("/list")
    public ResponseEntity<List<ParkingSession>> listParkedCars() {
        return ResponseEntity.ok(parkingSessionService.getAllParkedCars());
    }
}
