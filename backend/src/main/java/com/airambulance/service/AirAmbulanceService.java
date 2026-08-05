package com.airambulance.service;

import com.airambulance.dto.AirAmbulanceRequest;
import com.airambulance.dto.AirAmbulanceResponse;
import com.airambulance.enums.AmbulanceStatus;

import java.util.List;

public interface AirAmbulanceService {
    AirAmbulanceResponse createAirAmbulance(AirAmbulanceRequest request);
    AirAmbulanceResponse updateAirAmbulance(Long id, AirAmbulanceRequest request);
    void deleteAirAmbulance(Long id);
    AirAmbulanceResponse getAirAmbulanceById(Long id);
    List<AirAmbulanceResponse> getAllAirAmbulances();
    List<AirAmbulanceResponse> getAvailableAirAmbulances();
    List<AirAmbulanceResponse> searchAirAmbulances(String query);
    AirAmbulanceResponse assignPilot(Long ambulanceId, Long pilotId);
    AirAmbulanceResponse updateStatus(Long ambulanceId, AmbulanceStatus status, Boolean availability);
}
