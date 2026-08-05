package com.airambulance.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String contactNumber;
    private String emergencyContact;
    private Boolean hasHelipad;
    private Integer totalICUBeds;
    private Integer availableICUBeds;
    private Long userId;
    private String userEmail;
}
