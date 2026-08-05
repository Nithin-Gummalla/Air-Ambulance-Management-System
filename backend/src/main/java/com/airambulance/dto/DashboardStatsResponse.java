package com.airambulance.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {

    // Admin Dashboard Counters
    private long totalHospitals;
    private long totalAmbulances;
    private long availableAmbulances;
    private long totalPatients;
    private long totalBookings;
    private long emergencyRequests;
    private long flightsToday;
    private long activeOrganTransports;
    private long activePilots;

    // Recent activity feeds
    private List<EmergencyBookingResponse> recentBookings;
    private List<AirAmbulanceResponse> activeAmbulanceList;
    private List<NotificationResponse> recentNotifications;
}
