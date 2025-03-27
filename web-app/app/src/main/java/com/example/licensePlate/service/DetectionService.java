package com.example.licensePlate.service;

import com.example.licensePlate.model.Detection;
import com.example.licensePlate.repository.DetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;


@Service
public class DetectionService {

    @Autowired
    private DetectionRepository detectionRepository;


    // Save a detection
    public Detection saveDetection(Detection detection) {

        return detectionRepository.save(detection);
    }



    public List<Detection> getNdetections(int numOfDetections) {
        return detectionRepository.findAll().stream()
                .sorted(Comparator.comparing(Detection::getDetectionDate).reversed())
                .limit(numOfDetections)
                .toList();

    }


}
