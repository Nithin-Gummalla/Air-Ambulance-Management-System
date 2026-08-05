package com.airambulance.repository;

import com.airambulance.entity.AirAmbulance;
import com.airambulance.entity.Pilot;
import com.airambulance.enums.AmbulanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirAmbulanceRepository extends JpaRepository<AirAmbulance, Long> {
    Optional<AirAmbulance> findByRegistrationNumber(String registrationNumber);
    List<AirAmbulance> findByAvailability(Boolean availability);
    List<AirAmbulance> findByStatus(AmbulanceStatus status);
    Optional<AirAmbulance> findByPilot(Pilot pilot);
    List<AirAmbulance> findByModelContainingIgnoreCaseOrCurrentLocationContainingIgnoreCase(String model, String currentLocation);
}
