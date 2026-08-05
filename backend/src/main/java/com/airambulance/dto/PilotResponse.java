package com.airambulance.dto;

import com.airambulance.enums.PilotStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PilotResponse {
    private Long id;
    private String name;
    private String licenseNumber;
    private Integer experienceYears;
    private String phone;
    private PilotStatus status;
    private Long userId;
    private String userEmail;
    private String assignedAmbulanceRegistration;
}
