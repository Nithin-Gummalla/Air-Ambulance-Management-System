package com.airambulance.dto;

import com.airambulance.enums.AmbulanceStatus;
import com.airambulance.enums.FuelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirAmbulanceRequest {

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Capacity is required")
    private Integer capacity;

    @NotBlank(message = "Current location is required")
    private String currentLocation;

    @NotNull(message = "Fuel status is required")
    private FuelStatus fuelStatus;

    @NotNull(message = "Status is required")
    private AmbulanceStatus status;

    private Boolean availability;
    private Long pilotId;
}
