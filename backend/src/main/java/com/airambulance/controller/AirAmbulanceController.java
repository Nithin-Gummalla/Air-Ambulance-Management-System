package com.airambulance.controller;

import com.airambulance.dto.AirAmbulanceRequest;
import com.airambulance.dto.AirAmbulanceResponse;
import com.airambulance.dto.MessageResponse;
import com.airambulance.enums.AmbulanceStatus;
import com.airambulance.service.AirAmbulanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ambulances")
@RequiredArgsConstructor
@Tag(name = "Air Ambulance Module", description = "Endpoints for fleet management, air ambulance registration, status and pilot assignments")
public class AirAmbulanceController {

    private final AirAmbulanceService airAmbulanceService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Register Air Ambulance", description = "Registers a new air ambulance in the fleet")
    public ResponseEntity<AirAmbulanceResponse> createAirAmbulance(@Valid @RequestBody AirAmbulanceRequest request) {
        return ResponseEntity.ok(airAmbulanceService.createAirAmbulance(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PILOT')")
    @Operation(summary = "Update Air Ambulance", description = "Updates details of an air ambulance")
    public ResponseEntity<AirAmbulanceResponse> updateAirAmbulance(@PathVariable Long id, @Valid @RequestBody AirAmbulanceRequest request) {
        return ResponseEntity.ok(airAmbulanceService.updateAirAmbulance(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Air Ambulance", description = "Deletes an air ambulance from fleet")
    public ResponseEntity<MessageResponse> deleteAirAmbulance(@PathVariable Long id) {
        airAmbulanceService.deleteAirAmbulance(id);
        return ResponseEntity.ok(new MessageResponse("Air Ambulance removed from fleet"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "View Air Ambulance Details", description = "Gets air ambulance by ID")
    public ResponseEntity<AirAmbulanceResponse> getAirAmbulanceById(@PathVariable Long id) {
        return ResponseEntity.ok(airAmbulanceService.getAirAmbulanceById(id));
    }

    @GetMapping
    @Operation(summary = "List All Air Ambulances", description = "Returns full fleet list")
    public ResponseEntity<List<AirAmbulanceResponse>> getAllAirAmbulances() {
        return ResponseEntity.ok(airAmbulanceService.getAllAirAmbulances());
    }

    @GetMapping("/available")
    @Operation(summary = "List Available Air Ambulances", description = "Returns air ambulances currently available for dispatch")
    public ResponseEntity<List<AirAmbulanceResponse>> getAvailableAirAmbulances() {
        return ResponseEntity.ok(airAmbulanceService.getAvailableAirAmbulances());
    }

    @GetMapping("/search")
    @Operation(summary = "Search Air Ambulances", description = "Search fleet by model or location")
    public ResponseEntity<List<AirAmbulanceResponse>> searchAirAmbulances(@RequestParam String query) {
        return ResponseEntity.ok(airAmbulanceService.searchAirAmbulances(query));
    }

    @PutMapping("/{id}/assign-pilot/{pilotId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign Pilot to Ambulance", description = "Assigns a designated pilot to an air ambulance")
    public ResponseEntity<AirAmbulanceResponse> assignPilot(@PathVariable Long id, @PathVariable Long pilotId) {
        return ResponseEntity.ok(airAmbulanceService.assignPilot(id, pilotId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PILOT')")
    @Operation(summary = "Update Ambulance Status", description = "Updates status and availability of an air ambulance")
    public ResponseEntity<AirAmbulanceResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam(required = false) AmbulanceStatus status,
            @RequestParam(required = false) Boolean availability) {
        return ResponseEntity.ok(airAmbulanceService.updateStatus(id, status, availability));
    }
}
