package com.airambulance.controller;

import com.airambulance.dto.MessageResponse;
import com.airambulance.dto.PatientRequest;
import com.airambulance.dto.PatientResponse;
import com.airambulance.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Module", description = "Endpoints for managing medical patient profiles")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "Register Patient", description = "Creates a new patient profile")
    public ResponseEntity<PatientResponse> createPatient(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.createPatient(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PATIENT') or hasRole('HOSPITAL')")
    @Operation(summary = "Update Patient Profile", description = "Updates details of a patient")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientRequest request) {
        return ResponseEntity.ok(patientService.updatePatient(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete Patient Profile", description = "Deletes a patient record")
    public ResponseEntity<MessageResponse> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(new MessageResponse("Patient record removed"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "View Patient Details", description = "Fetches patient profile by ID")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOSPITAL') or hasRole('PILOT')")
    @Operation(summary = "List All Patients", description = "Returns all patient records")
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOSPITAL') or hasRole('PILOT')")
    @Operation(summary = "Search Patients", description = "Search patients by name")
    public ResponseEntity<List<PatientResponse>> searchPatients(@RequestParam String query) {
        return ResponseEntity.ok(patientService.searchPatients(query));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Patient by User ID", description = "Fetches patient profile associated with user ID")
    public ResponseEntity<PatientResponse> getPatientByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(patientService.getPatientByUserId(userId));
    }
}
