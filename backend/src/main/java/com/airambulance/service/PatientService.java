package com.airambulance.service;

import com.airambulance.dto.PatientRequest;
import com.airambulance.dto.PatientResponse;

import java.util.List;

public interface PatientService {
    PatientResponse createPatient(PatientRequest request);
    PatientResponse updatePatient(Long id, PatientRequest request);
    void deletePatient(Long id);
    PatientResponse getPatientById(Long id);
    List<PatientResponse> getAllPatients();
    List<PatientResponse> searchPatients(String query);
    PatientResponse getPatientByUserId(Long userId);
}
