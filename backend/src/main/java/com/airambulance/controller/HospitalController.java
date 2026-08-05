package com.airambulance.controller;

import com.airambulance.dto.HospitalRequest;
import com.airambulance.dto.HospitalResponse;
import com.airambulance.dto.MessageResponse;
import com.airambulance.service.HospitalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
@RequiredArgsConstructor
@Tag(name = "Hospital Module", description = "Endpoints for managing hospitals and medical centers")
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOSPITAL')")
    @Operation(summary = "Register Hospital", description = "Creates a new hospital profile in the network")
    public ResponseEntity<HospitalResponse> createHospital(@Valid @RequestBody HospitalRequest hospitalRequest) {
        return ResponseEntity.ok(hospitalService.createHospital(hospitalRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOSPITAL')")
    @Operation(summary = "Update Hospital", description = "Updates details of an existing hospital")
    public ResponseEntity<HospitalResponse> updateHospital(@PathVariable Long id, @Valid @RequestBody HospitalRequest hospitalRequest) {
        return ResponseEntity.ok(hospitalService.updateHospital(id, hospitalRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Hospital", description = "Removes a hospital from the network")
    public ResponseEntity<MessageResponse> deleteHospital(@PathVariable Long id) {
        hospitalService.deleteHospital(id);
        return ResponseEntity.ok(new MessageResponse("Hospital deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "View Hospital Details", description = "Fetches a hospital by ID")
    public ResponseEntity<HospitalResponse> getHospitalById(@PathVariable Long id) {
        return ResponseEntity.ok(hospitalService.getHospitalById(id));
    }

    @GetMapping
    @Operation(summary = "List All Hospitals", description = "Fetches all registered hospitals")
    public ResponseEntity<List<HospitalResponse>> getAllHospitals() {
        return ResponseEntity.ok(hospitalService.getAllHospitals());
    }

    @GetMapping("/search")
    @Operation(summary = "Search Hospitals", description = "Search hospitals by name or city")
    public ResponseEntity<List<HospitalResponse>> searchHospitals(@RequestParam String query) {
        return ResponseEntity.ok(hospitalService.searchHospitals(query));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Hospital by User ID", description = "Fetches hospital associated with a user account")
    public ResponseEntity<HospitalResponse> getHospitalByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(hospitalService.getHospitalByUserId(userId));
    }
}
