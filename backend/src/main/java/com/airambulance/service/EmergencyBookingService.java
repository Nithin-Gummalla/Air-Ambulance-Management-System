package com.airambulance.service;

import com.airambulance.dto.EmergencyBookingRequest;
import com.airambulance.dto.EmergencyBookingResponse;
import com.airambulance.dto.UpdateBookingStatusRequest;

import java.util.List;

public interface EmergencyBookingService {
    EmergencyBookingResponse createBooking(EmergencyBookingRequest request, String currentUserEmail);
    EmergencyBookingResponse updateBookingStatus(Long id, UpdateBookingStatusRequest statusRequest);
    EmergencyBookingResponse assignAirAmbulance(Long bookingId, Long airAmbulanceId);
    EmergencyBookingResponse getBookingById(Long id);
    List<EmergencyBookingResponse> getAllBookings();
    List<EmergencyBookingResponse> getBookingsByPatient(Long patientId);
    List<EmergencyBookingResponse> getBookingsByHospital(Long hospitalId);
    List<EmergencyBookingResponse> getBookingsByPilot(Long pilotId);
    void cancelBooking(Long id);
}
