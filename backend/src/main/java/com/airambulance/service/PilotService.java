package com.airambulance.service;

import com.airambulance.dto.PilotRequest;
import com.airambulance.dto.PilotResponse;
import com.airambulance.enums.PilotStatus;

import java.util.List;

public interface PilotService {
    PilotResponse createPilot(PilotRequest pilotRequest);
    PilotResponse updatePilot(Long id, PilotRequest pilotRequest);
    void deletePilot(Long id);
    PilotResponse getPilotById(Long id);
    List<PilotResponse> getAllPilots();
    List<PilotResponse> getPilotsByStatus(PilotStatus status);
    PilotResponse getPilotByUserId(Long userId);
    PilotResponse updatePilotStatus(Long id, PilotStatus status);
}
