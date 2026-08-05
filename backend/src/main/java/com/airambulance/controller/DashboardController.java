package com.airambulance.controller;

import com.airambulance.dto.DashboardStatsResponse;
import com.airambulance.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard Module", description = "Endpoints for aggregated metrics across Admin, Hospital, Pilot, and Patient roles")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Admin Dashboard Metrics", description = "Aggregates total hospitals, fleet status, emergency requests, flights today, and active organ transports")
    public ResponseEntity<DashboardStatsResponse> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboardStats());
    }

    @GetMapping("/hospital")
    @PreAuthorize("hasRole('HOSPITAL') or hasRole('ADMIN')")
    @Operation(summary = "Hospital Dashboard Metrics", description = "Provides hospital-specific emergency requests, incoming air ambulances, and organ transports")
    public ResponseEntity<DashboardStatsResponse> getHospitalDashboard(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(dashboardService.getHospitalDashboardStats(email));
    }

    @GetMapping("/pilot")
    @PreAuthorize("hasRole('PILOT') or hasRole('ADMIN')")
    @Operation(summary = "Pilot Dashboard Metrics", description = "Provides pilot mission metrics, assigned air ambulance status, and flight logs")
    public ResponseEntity<DashboardStatsResponse> getPilotDashboard(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(dashboardService.getPilotDashboardStats(email));
    }

    @GetMapping("/patient")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    @Operation(summary = "Patient Dashboard Metrics", description = "Provides patient booking history and active emergency dispatch updates")
    public ResponseEntity<DashboardStatsResponse> getPatientDashboard(Authentication authentication) {
        String email = authentication != null ? authentication.getName() : null;
        return ResponseEntity.ok(dashboardService.getPatientDashboardStats(email));
    }
}
