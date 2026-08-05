package com.airambulance.dto;

import com.airambulance.enums.BookingStatus;
import com.airambulance.enums.EmergencyLevel;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyBookingResponse {
    private Long id;
    private PatientResponse patient;
    private String pickupLocation;
    private HospitalResponse destinationHospital;
    private AirAmbulanceResponse airAmbulance;
    private EmergencyLevel emergencyLevel;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private BookingStatus status;
    private String notes;
    private LocalDateTime createdAt;
}
