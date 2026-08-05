package com.airambulance.service;

import com.airambulance.dto.DashboardStatsResponse;

public interface DashboardService {
    DashboardStatsResponse getAdminDashboardStats();
    DashboardStatsResponse getHospitalDashboardStats(String email);
    DashboardStatsResponse getPilotDashboardStats(String email);
    DashboardStatsResponse getPatientDashboardStats(String email);
}
