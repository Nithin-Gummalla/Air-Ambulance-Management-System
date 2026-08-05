package com.airambulance.dto;

import com.airambulance.enums.OrganPriority;
import com.airambulance.enums.OrganTransportStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganTransportResponse {
    private Long id;
    private String organType;
    private HospitalResponse sourceHospital;
    private HospitalResponse destinationHospital;
    private AirAmbulanceResponse airAmbulance;
    private OrganPriority priority;
    private String estimatedTimeOfArrival;
    private OrganTransportStatus status;
    private String notes;
    private LocalDateTime createdAt;
}
