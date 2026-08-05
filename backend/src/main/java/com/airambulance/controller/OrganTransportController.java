package com.airambulance.controller;

import com.airambulance.dto.MessageResponse;
import com.airambulance.dto.OrganTransportRequest;
import com.airambulance.dto.OrganTransportResponse;
import com.airambulance.service.OrganTransportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organ-transports")
@RequiredArgsConstructor
@Tag(name = "Organ Transport Module", description = "Endpoints for organ transport requests, hospital-to-hospital logistics, and priority tracking")
public class OrganTransportController {

    private final OrganTransportService organTransportService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOSPITAL')")
    @Operation(summary = "Create Organ Transport Request", description = "Schedules a high-priority organ transport between hospitals")
    public ResponseEntity<OrganTransportResponse> createOrganTransport(@Valid @RequestBody OrganTransportRequest request) {
        return ResponseEntity.ok(organTransportService.createOrganTransport(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOSPITAL') or hasRole('PILOT')")
    @Operation(summary = "Update Organ Transport", description = "Updates status, ETA or details of an organ transport")
    public ResponseEntity<OrganTransportResponse> updateOrganTransport(@PathVariable Long id, @Valid @RequestBody OrganTransportRequest request) {
        return ResponseEntity.ok(organTransportService.updateOrganTransport(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Organ Transport", description = "Deletes an organ transport entry")
    public ResponseEntity<MessageResponse> deleteOrganTransport(@PathVariable Long id) {
        organTransportService.deleteOrganTransport(id);
        return ResponseEntity.ok(new MessageResponse("Organ transport record deleted"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "View Organ Transport Details", description = "Fetches organ transport entry by ID")
    public ResponseEntity<OrganTransportResponse> getOrganTransportById(@PathVariable Long id) {
        return ResponseEntity.ok(organTransportService.getOrganTransportById(id));
    }

    @GetMapping
    @Operation(summary = "List All Organ Transports", description = "Returns all scheduled and active organ transport missions")
    public ResponseEntity<List<OrganTransportResponse>> getAllOrganTransports() {
        return ResponseEntity.ok(organTransportService.getAllOrganTransports());
    }

    @GetMapping("/hospital/{hospitalId}")
    @Operation(summary = "Get Hospital Organ Transports", description = "Returns organ transport requests involving a specific hospital")
    public ResponseEntity<List<OrganTransportResponse>> getOrganTransportsByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(organTransportService.getOrganTransportsByHospital(hospitalId));
    }
}
