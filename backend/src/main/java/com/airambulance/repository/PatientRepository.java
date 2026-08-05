package com.airambulance.repository;

import com.airambulance.entity.Patient;
import com.airambulance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUser(User user);
    Optional<Patient> findByUserId(Long userId);
    List<Patient> findByNameContainingIgnoreCase(String name);
}
