package com.airambulance.dto;

import com.airambulance.enums.EmergencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyBookingRequest {

    private Long patientId; // optional if submitting as logged in patient

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @NotNull(message = "Destination hospital ID is required")
    private Long destinationHospitalId;

    private Long airAmbulanceId; // Optional on initial request

    @NotNull(message = "Emergency level is required")
    private EmergencyLevel emergencyLevel;

    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private String notes;

    // Direct Patient Info fields if patient profile doesn't exist yet
    private String patientName;
    private Integer patientAge;
    private String patientGender;
    private String patientBloodGroup;
    private String emergencyContact;
    private String medicalCondition;
}
