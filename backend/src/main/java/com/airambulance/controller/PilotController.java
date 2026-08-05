package com.airambulance.controller;

import com.airambulance.dto.MessageResponse;
import com.airambulance.dto.PilotRequest;
import com.airambulance.dto.PilotResponse;
import com.airambulance.enums.PilotStatus;
import com.airambulance.service.PilotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pilots")
@RequiredArgsConstructor
@Tag(name = "Pilot Module", description = "Endpoints for managing pilots, license profiles, and status updates")
public class PilotController {

    private final PilotService pilotService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register Pilot", description = "Creates a new pilot record")
    public ResponseEntity<PilotResponse> createPilot(@Valid @RequestBody PilotRequest request) {
        return ResponseEntity.ok(pilotService.createPilot(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PILOT')")
    @Operation(summary = "Update Pilot Profile", description = "Updates details of a pilot")
    public ResponseEntity<PilotResponse> updatePilot(@PathVariable Long id, @Valid @RequestBody PilotRequest request) {
        return ResponseEntity.ok(pilotService.updatePilot(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Pilot", description = "Deletes a pilot record")
    public ResponseEntity<MessageResponse> deletePilot(@PathVariable Long id) {
        pilotService.deletePilot(id);
        return ResponseEntity.ok(new MessageResponse("Pilot removed successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "View Pilot Details", description = "Fetches pilot by ID")
    public ResponseEntity<PilotResponse> getPilotById(@PathVariable Long id) {
        return ResponseEntity.ok(pilotService.getPilotById(id));
    }

    @GetMapping
    @Operation(summary = "List All Pilots", description = "Returns list of all registered pilots")
    public ResponseEntity<List<PilotResponse>> getAllPilots() {
        return ResponseEntity.ok(pilotService.getAllPilots());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filter Pilots by Status", description = "Filter pilots by duty status (AVAILABLE, ON_MISSION, OFF_DUTY)")
    public ResponseEntity<List<PilotResponse>> getPilotsByStatus(@PathVariable PilotStatus status) {
        return ResponseEntity.ok(pilotService.getPilotsByStatus(status));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Pilot by User ID", description = "Fetches pilot details associated with a user account")
    public ResponseEntity<PilotResponse> getPilotByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(pilotService.getPilotByUserId(userId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PILOT')")
    @Operation(summary = "Update Pilot Duty Status", description = "Toggles pilot duty status")
    public ResponseEntity<PilotResponse> updatePilotStatus(@PathVariable Long id, @RequestParam PilotStatus status) {
        return ResponseEntity.ok(pilotService.updatePilotStatus(id, status));
    }
}
