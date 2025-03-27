package com.example.licensePlate.repository;

import com.example.licensePlate.model.Detection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DetectionRepository extends JpaRepository<Detection, Long> {

}
