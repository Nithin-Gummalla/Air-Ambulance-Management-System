package com.airambulance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignAmbulanceRequest {

    @NotNull(message = "Air ambulance ID is required")
    private Long airAmbulanceId;
}
