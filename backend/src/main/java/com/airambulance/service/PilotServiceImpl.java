package com.airambulance.service;

import com.airambulance.dto.PilotRequest;
import com.airambulance.dto.PilotResponse;
import com.airambulance.entity.AirAmbulance;
import com.airambulance.entity.Pilot;
import com.airambulance.entity.User;
import com.airambulance.enums.PilotStatus;
import com.airambulance.exception.ResourceNotFoundException;
import com.airambulance.repository.AirAmbulanceRepository;
import com.airambulance.repository.PilotRepository;
import com.airambulance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PilotServiceImpl implements PilotService {

    private final PilotRepository pilotRepository;
    private final UserRepository userRepository;
    private final AirAmbulanceRepository airAmbulanceRepository;

    @Override
    @Transactional
    public PilotResponse createPilot(PilotRequest request) {
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
        }

        Pilot pilot = Pilot.builder()
                .name(request.getName())
                .licenseNumber(request.getLicenseNumber())
                .experienceYears(request.getExperienceYears())
                .phone(request.getPhone())
                .status(request.getStatus() != null ? request.getStatus() : PilotStatus.AVAILABLE)
                .user(user)
                .build();

        Pilot saved = pilotRepository.save(pilot);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public PilotResponse updatePilot(Long id, PilotRequest request) {
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with ID: " + id));

        pilot.setName(request.getName());
        pilot.setLicenseNumber(request.getLicenseNumber());
        pilot.setExperienceYears(request.getExperienceYears());
        pilot.setPhone(request.getPhone());
        if (request.getStatus() != null) pilot.setStatus(request.getStatus());

        Pilot updated = pilotRepository.save(pilot);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deletePilot(Long id) {
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with ID: " + id));
        pilotRepository.delete(pilot);
    }

    @Override
    public PilotResponse getPilotById(Long id) {
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with ID: " + id));
        return mapToResponse(pilot);
    }

    @Override
    public List<PilotResponse> getAllPilots() {
        return pilotRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PilotResponse> getPilotsByStatus(PilotStatus status) {
        return pilotRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PilotResponse getPilotByUserId(Long userId) {
        Pilot pilot = pilotRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot profile not found for user ID: " + userId));
        return mapToResponse(pilot);
    }

    @Override
    @Transactional
    public PilotResponse updatePilotStatus(Long id, PilotStatus status) {
        Pilot pilot = pilotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with ID: " + id));
        pilot.setStatus(status);
        return mapToResponse(pilotRepository.save(pilot));
    }

    private PilotResponse mapToResponse(Pilot pilot) {
        Optional<AirAmbulance> amb = airAmbulanceRepository.findByPilot(pilot);
        return PilotResponse.builder()
                .id(pilot.getId())
                .name(pilot.getName())
                .licenseNumber(pilot.getLicenseNumber())
                .experienceYears(pilot.getExperienceYears())
                .phone(pilot.getPhone())
                .status(pilot.getStatus())
                .userId(pilot.getUser() != null ? pilot.getUser().getId() : null)
                .userEmail(pilot.getUser() != null ? pilot.getUser().getEmail() : null)
                .assignedAmbulanceRegistration(amb.map(AirAmbulance::getRegistrationNumber).orElse("None"))
                .build();
    }
}
