package com.airambulance.controller;

import com.airambulance.dto.AssignAmbulanceRequest;
import com.airambulance.dto.EmergencyBookingRequest;
import com.airambulance.dto.EmergencyBookingResponse;
import com.airambulance.dto.MessageResponse;
import com.airambulance.dto.UpdateBookingStatusRequest;
import com.airambulance.service.EmergencyBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Emergency Booking Module", description = "Endpoints for emergency transport dispatch, booking status workflow, and ambulance assignment")
public class EmergencyBookingController {

    private final EmergencyBookingService bookingService;

    @PostMapping
    @Operation(summary = "Request Emergency Transport", description = "Creates a new emergency air transport request and auto-assigns an available air ambulance")
    public ResponseEntity<EmergencyBookingResponse> createBooking(
            @Valid @RequestBody EmergencyBookingRequest request,
            Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(bookingService.createBooking(request, email));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOSPITAL') or hasRole('PILOT')")
    @Operation(summary = "Update Booking Status", description = "Updates status of an emergency booking (APPROVED, IN_TRANSIT, COMPLETED, CANCELLED)")
    public ResponseEntity<EmergencyBookingResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingStatusRequest statusRequest) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(id, statusRequest));
    }

    @PutMapping("/{id}/assign-ambulance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HOSPITAL')")
    @Operation(summary = "Assign Air Ambulance to Booking", description = "Manually assigns a specific air ambulance to an emergency booking")
    public ResponseEntity<EmergencyBookingResponse> assignAmbulance(
            @PathVariable Long id,
            @Valid @RequestBody AssignAmbulanceRequest request) {
        return ResponseEntity.ok(bookingService.assignAirAmbulance(id, request.getAirAmbulanceId()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "View Booking Details", description = "Fetches emergency booking by ID")
    public ResponseEntity<EmergencyBookingResponse> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping
    @Operation(summary = "List All Bookings", description = "Returns all emergency transport bookings")
    public ResponseEntity<List<EmergencyBookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get Patient Bookings", description = "Returns emergency bookings for a specific patient")
    public ResponseEntity<List<EmergencyBookingResponse>> getBookingsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(bookingService.getBookingsByPatient(patientId));
    }

    @GetMapping("/hospital/{hospitalId}")
    @Operation(summary = "Get Hospital Bookings", description = "Returns inbound emergency bookings for a destination hospital")
    public ResponseEntity<List<EmergencyBookingResponse>> getBookingsByHospital(@PathVariable Long hospitalId) {
        return ResponseEntity.ok(bookingService.getBookingsByHospital(hospitalId));
    }

    @GetMapping("/pilot/{pilotId}")
    @Operation(summary = "Get Pilot Assigned Bookings", description = "Returns assigned emergency bookings for a pilot")
    public ResponseEntity<List<EmergencyBookingResponse>> getBookingsByPilot(@PathVariable Long pilotId) {
        return ResponseEntity.ok(bookingService.getBookingsByPilot(pilotId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel Emergency Booking", description = "Cancels an emergency transport booking")
    public ResponseEntity<MessageResponse> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok(new MessageResponse("Booking cancelled successfully"));
    }
}
