package com.airambulance.service;

import com.airambulance.dto.AirAmbulanceRequest;
import com.airambulance.dto.AirAmbulanceResponse;
import com.airambulance.dto.PilotResponse;
import com.airambulance.entity.AirAmbulance;
import com.airambulance.entity.Pilot;
import com.airambulance.enums.AmbulanceStatus;
import com.airambulance.exception.ResourceNotFoundException;
import com.airambulance.repository.AirAmbulanceRepository;
import com.airambulance.repository.PilotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirAmbulanceServiceImpl implements AirAmbulanceService {

    private final AirAmbulanceRepository airAmbulanceRepository;
    private final PilotRepository pilotRepository;

    @Override
    @Transactional
    public AirAmbulanceResponse createAirAmbulance(AirAmbulanceRequest request) {
        Pilot pilot = null;
        if (request.getPilotId() != null) {
            pilot = pilotRepository.findById(request.getPilotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with ID: " + request.getPilotId()));
        }

        AirAmbulance ambulance = AirAmbulance.builder()
                .registrationNumber(request.getRegistrationNumber())
                .model(request.getModel())
                .capacity(request.getCapacity())
                .currentLocation(request.getCurrentLocation())
                .fuelStatus(request.getFuelStatus())
                .status(request.getStatus() != null ? request.getStatus() : AmbulanceStatus.AVAILABLE)
                .availability(request.getAvailability() != null ? request.getAvailability() : true)
                .pilot(pilot)
                .build();

        AirAmbulance saved = airAmbulanceRepository.save(ambulance);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public AirAmbulanceResponse updateAirAmbulance(Long id, AirAmbulanceRequest request) {
        AirAmbulance ambulance = airAmbulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + id));

        ambulance.setRegistrationNumber(request.getRegistrationNumber());
        ambulance.setModel(request.getModel());
        ambulance.setCapacity(request.getCapacity());
        ambulance.setCurrentLocation(request.getCurrentLocation());
        ambulance.setFuelStatus(request.getFuelStatus());
        if (request.getStatus() != null) ambulance.setStatus(request.getStatus());
        if (request.getAvailability() != null) ambulance.setAvailability(request.getAvailability());

        if (request.getPilotId() != null) {
            Pilot pilot = pilotRepository.findById(request.getPilotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with ID: " + request.getPilotId()));
            ambulance.setPilot(pilot);
        }

        AirAmbulance updated = airAmbulanceRepository.save(ambulance);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteAirAmbulance(Long id) {
        AirAmbulance ambulance = airAmbulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + id));
        airAmbulanceRepository.delete(ambulance);
    }

    @Override
    public AirAmbulanceResponse getAirAmbulanceById(Long id) {
        AirAmbulance ambulance = airAmbulanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + id));
        return mapToResponse(ambulance);
    }

    @Override
    public List<AirAmbulanceResponse> getAllAirAmbulances() {
        return airAmbulanceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AirAmbulanceResponse> getAvailableAirAmbulances() {
        return airAmbulanceRepository.findByAvailability(true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AirAmbulanceResponse> searchAirAmbulances(String query) {
        return airAmbulanceRepository.findByModelContainingIgnoreCaseOrCurrentLocationContainingIgnoreCase(query, query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AirAmbulanceResponse assignPilot(Long ambulanceId, Long pilotId) {
        AirAmbulance ambulance = airAmbulanceRepository.findById(ambulanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + ambulanceId));
        Pilot pilot = pilotRepository.findById(pilotId)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with ID: " + pilotId));

        ambulance.setPilot(pilot);
        return mapToResponse(airAmbulanceRepository.save(ambulance));
    }

    @Override
    @Transactional
    public AirAmbulanceResponse updateStatus(Long ambulanceId, AmbulanceStatus status, Boolean availability) {
        AirAmbulance ambulance = airAmbulanceRepository.findById(ambulanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + ambulanceId));
        if (status != null) ambulance.setStatus(status);
        if (availability != null) ambulance.setAvailability(availability);
        return mapToResponse(airAmbulanceRepository.save(ambulance));
    }

    private AirAmbulanceResponse mapToResponse(AirAmbulance amb) {
        PilotResponse pilotRes = null;
        if (amb.getPilot() != null) {
            Pilot p = amb.getPilot();
            pilotRes = PilotResponse.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .licenseNumber(p.getLicenseNumber())
                    .experienceYears(p.getExperienceYears())
                    .phone(p.getPhone())
                    .status(p.getStatus())
                    .userId(p.getUser() != null ? p.getUser().getId() : null)
                    .userEmail(p.getUser() != null ? p.getUser().getEmail() : null)
                    .assignedAmbulanceRegistration(amb.getRegistrationNumber())
                    .build();
        }

        return AirAmbulanceResponse.builder()
                .id(amb.getId())
                .registrationNumber(amb.getRegistrationNumber())
                .model(amb.getModel())
                .capacity(amb.getCapacity())
                .currentLocation(amb.getCurrentLocation())
                .fuelStatus(amb.getFuelStatus())
                .status(amb.getStatus())
                .availability(amb.getAvailability())
                .pilot(pilotRes)
                .build();
    }
}
