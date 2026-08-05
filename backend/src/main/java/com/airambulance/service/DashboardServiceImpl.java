package com.airambulance.service;

import com.airambulance.dto.DashboardStatsResponse;
import com.airambulance.enums.BookingStatus;
import com.airambulance.enums.OrganTransportStatus;
import com.airambulance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final HospitalRepository hospitalRepository;
    private final AirAmbulanceRepository airAmbulanceRepository;
    private final PilotRepository pilotRepository;
    private final PatientRepository patientRepository;
    private final EmergencyBookingRepository emergencyBookingRepository;
    private final OrganTransportRepository organTransportRepository;
    private final EmergencyBookingService emergencyBookingService;
    private final AirAmbulanceService airAmbulanceService;
    private final NotificationService notificationService;

    @Override
    public DashboardStatsResponse getAdminDashboardStats() {
        long totalHospitals = hospitalRepository.count();
        long totalAmbulances = airAmbulanceRepository.count();
        long availableAmbulances = airAmbulanceRepository.findByAvailability(true).size();
        long totalPatients = patientRepository.count();
        long totalBookings = emergencyBookingRepository.count();
        long emergencyRequests = emergencyBookingRepository.countByStatus(BookingStatus.REQUESTED);
        long flightsToday = emergencyBookingRepository.countByBookingDate(LocalDate.now());
        long activeOrganTransports = organTransportRepository.findByStatus(OrganTransportStatus.IN_TRANSIT).size();
        long activePilots = pilotRepository.count();

        return DashboardStatsResponse.builder()
                .totalHospitals(totalHospitals)
                .totalAmbulances(totalAmbulances)
                .availableAmbulances(availableAmbulances)
                .totalPatients(totalPatients)
                .totalBookings(totalBookings)
                .emergencyRequests(emergencyRequests)
                .flightsToday(flightsToday)
                .activeOrganTransports(activeOrganTransports)
                .activePilots(activePilots)
                .recentBookings(emergencyBookingService.getAllBookings().stream().limit(5).toList())
                .activeAmbulanceList(airAmbulanceService.getAvailableAirAmbulances())
                .recentNotifications(notificationService.getAllNotifications().stream().limit(5).toList())
                .build();
    }

    @Override
    public DashboardStatsResponse getHospitalDashboardStats(String email) {
        return getAdminDashboardStats();
    }

    @Override
    public DashboardStatsResponse getPilotDashboardStats(String email) {
        return getAdminDashboardStats();
    }

    @Override
    public DashboardStatsResponse getPatientDashboardStats(String email) {
        return getAdminDashboardStats();
    }
}
