package com.airambulance.service;

import com.airambulance.dto.HospitalRequest;
import com.airambulance.dto.HospitalResponse;

import java.util.List;

public interface HospitalService {
    HospitalResponse createHospital(HospitalRequest hospitalRequest);
    HospitalResponse updateHospital(Long id, HospitalRequest hospitalRequest);
    void deleteHospital(Long id);
    HospitalResponse getHospitalById(Long id);
    List<HospitalResponse> getAllHospitals();
    List<HospitalResponse> searchHospitals(String query);
    HospitalResponse getHospitalByUserId(Long userId);
}
