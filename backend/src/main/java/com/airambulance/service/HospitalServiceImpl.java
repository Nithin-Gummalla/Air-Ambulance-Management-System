package com.airambulance.service;

import com.airambulance.dto.HospitalRequest;
import com.airambulance.dto.HospitalResponse;
import com.airambulance.entity.Hospital;
import com.airambulance.entity.User;
import com.airambulance.exception.ResourceNotFoundException;
import com.airambulance.repository.HospitalRepository;
import com.airambulance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public HospitalResponse createHospital(HospitalRequest request) {
        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
        }

        Hospital hospital = Hospital.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .contactNumber(request.getContactNumber())
                .emergencyContact(request.getEmergencyContact())
                .hasHelipad(request.getHasHelipad() != null ? request.getHasHelipad() : true)
                .totalICUBeds(request.getTotalICUBeds() != null ? request.getTotalICUBeds() : 0)
                .availableICUBeds(request.getAvailableICUBeds() != null ? request.getAvailableICUBeds() : 0)
                .user(user)
                .build();

        Hospital saved = hospitalRepository.save(hospital);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public HospitalResponse updateHospital(Long id, HospitalRequest request) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + id));

        hospital.setName(request.getName());
        hospital.setAddress(request.getAddress());
        hospital.setCity(request.getCity());
        hospital.setState(request.getState());
        hospital.setContactNumber(request.getContactNumber());
        hospital.setEmergencyContact(request.getEmergencyContact());
        if (request.getHasHelipad() != null) hospital.setHasHelipad(request.getHasHelipad());
        if (request.getTotalICUBeds() != null) hospital.setTotalICUBeds(request.getTotalICUBeds());
        if (request.getAvailableICUBeds() != null) hospital.setAvailableICUBeds(request.getAvailableICUBeds());

        Hospital updated = hospitalRepository.save(hospital);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteHospital(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + id));
        hospitalRepository.delete(hospital);
    }

    @Override
    public HospitalResponse getHospitalById(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + id));
        return mapToResponse(hospital);
    }

    @Override
    public List<HospitalResponse> getAllHospitals() {
        return hospitalRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<HospitalResponse> searchHospitals(String query) {
        return hospitalRepository.findByNameContainingIgnoreCaseOrCityContainingIgnoreCase(query, query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HospitalResponse getHospitalByUserId(Long userId) {
        Hospital hospital = hospitalRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital profile not found for user ID: " + userId));
        return mapToResponse(hospital);
    }

    private HospitalResponse mapToResponse(Hospital hospital) {
        return HospitalResponse.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .address(hospital.getAddress())
                .city(hospital.getCity())
                .state(hospital.getState())
                .contactNumber(hospital.getContactNumber())
                .emergencyContact(hospital.getEmergencyContact())
                .hasHelipad(hospital.getHasHelipad())
                .totalICUBeds(hospital.getTotalICUBeds())
                .availableICUBeds(hospital.getAvailableICUBeds())
                .userId(hospital.getUser() != null ? hospital.getUser().getId() : null)
                .userEmail(hospital.getUser() != null ? hospital.getUser().getEmail() : null)
                .build();
    }
}
