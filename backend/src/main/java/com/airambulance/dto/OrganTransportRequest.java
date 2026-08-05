package com.airambulance.dto;

import com.airambulance.enums.OrganPriority;
import com.airambulance.enums.OrganTransportStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganTransportRequest {

    @NotBlank(message = "Organ type is required")
    private String organType;

    @NotNull(message = "Source hospital ID is required")
    private Long sourceHospitalId;

    @NotNull(message = "Destination hospital ID is required")
    private Long destinationHospitalId;

    private Long airAmbulanceId;

    @NotNull(message = "Priority is required")
    private OrganPriority priority;

    private String estimatedTimeOfArrival;
    private OrganTransportStatus status;
    private String notes;
}
