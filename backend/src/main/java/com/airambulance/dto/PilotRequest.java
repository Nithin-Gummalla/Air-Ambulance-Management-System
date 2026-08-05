package com.airambulance.dto;

import com.airambulance.enums.PilotStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PilotRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    @NotNull(message = "Experience is required")
    private Integer experienceYears;

    @NotBlank(message = "Phone is required")
    private String phone;

    private PilotStatus status;
    private Long userId;
}
