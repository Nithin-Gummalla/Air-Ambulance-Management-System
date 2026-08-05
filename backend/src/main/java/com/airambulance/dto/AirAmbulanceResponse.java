package com.airambulance.dto;

import com.airambulance.enums.AmbulanceStatus;
import com.airambulance.enums.FuelStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AirAmbulanceResponse {
    private Long id;
    private String registrationNumber;
    private String model;
    private Integer capacity;
    private String currentLocation;
    private FuelStatus fuelStatus;
    private AmbulanceStatus status;
    private Boolean availability;
    private PilotResponse pilot;
}
