package com.example.licensePlate.repository;

import com.example.licensePlate.model.ParkingSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    Optional<ParkingSession> findByLicensePlate(String licensePlate);

    @Transactional
    void deleteByLicensePlate(String licensePlate);
}
