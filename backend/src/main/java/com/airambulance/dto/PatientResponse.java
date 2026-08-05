package com.airambulance.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientResponse {
    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private String bloodGroup;
    private String emergencyContact;
    private String medicalCondition;
    private Long userId;
    private String userEmail;
}
