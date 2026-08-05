package com.airambulance.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalRequest {

    @NotBlank(message = "Hospital name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    private String state;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @NotBlank(message = "Emergency contact is required")
    private String emergencyContact;

    private Boolean hasHelipad;
    private Integer totalICUBeds;
    private Integer availableICUBeds;
    private Long userId;
}
