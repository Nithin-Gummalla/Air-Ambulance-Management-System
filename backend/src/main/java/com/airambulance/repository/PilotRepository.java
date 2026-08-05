package com.airambulance.repository;

import com.airambulance.entity.Pilot;
import com.airambulance.entity.User;
import com.airambulance.enums.PilotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Long> {
    Optional<Pilot> findByUser(User user);
    Optional<Pilot> findByUserId(Long userId);
    Optional<Pilot> findByLicenseNumber(String licenseNumber);
    List<Pilot> findByStatus(PilotStatus status);
}
