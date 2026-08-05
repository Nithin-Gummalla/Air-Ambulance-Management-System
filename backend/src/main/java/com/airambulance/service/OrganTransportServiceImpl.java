package com.airambulance.service;

import com.airambulance.dto.OrganTransportRequest;
import com.airambulance.dto.OrganTransportResponse;
import com.airambulance.entity.AirAmbulance;
import com.airambulance.entity.Hospital;
import com.airambulance.entity.Notification;
import com.airambulance.entity.OrganTransport;
import com.airambulance.enums.NotificationType;
import com.airambulance.enums.OrganTransportStatus;
import com.airambulance.exception.ResourceNotFoundException;
import com.airambulance.repository.AirAmbulanceRepository;
import com.airambulance.repository.HospitalRepository;
import com.airambulance.repository.NotificationRepository;
import com.airambulance.repository.OrganTransportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganTransportServiceImpl implements OrganTransportService {

    private final OrganTransportRepository organTransportRepository;
    private final HospitalRepository hospitalRepository;
    private final AirAmbulanceRepository airAmbulanceRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public OrganTransportResponse createOrganTransport(OrganTransportRequest request) {
        Hospital source = hospitalRepository.findById(request.getSourceHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Source Hospital not found with ID: " + request.getSourceHospitalId()));
        Hospital destination = hospitalRepository.findById(request.getDestinationHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination Hospital not found with ID: " + request.getDestinationHospitalId()));

        AirAmbulance amb = null;
        if (request.getAirAmbulanceId() != null) {
            amb = airAmbulanceRepository.findById(request.getAirAmbulanceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + request.getAirAmbulanceId()));
        }

        OrganTransport transport = OrganTransport.builder()
                .organType(request.getOrganType())
                .sourceHospital(source)
                .destinationHospital(destination)
                .airAmbulance(amb)
                .priority(request.getPriority())
                .estimatedTimeOfArrival(request.getEstimatedTimeOfArrival() != null ? request.getEstimatedTimeOfArrival() : "Pending ETA")
                .status(request.getStatus() != null ? request.getStatus() : OrganTransportStatus.SCHEDULED)
                .notes(request.getNotes())
                .build();

        OrganTransport saved = organTransportRepository.save(transport);

        // Notify destination hospital
        Notification n = Notification.builder()
                .title("High Priority Organ Transport")
                .message("Organ transport (" + request.getOrganType() + ") scheduled from " + source.getName() + " to " + destination.getName())
                .type(NotificationType.ORGAN_TRANSPORT_ALERT)
                .recipientUser(destination.getUser())
                .isRead(false)
                .build();
        notificationRepository.save(n);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public OrganTransportResponse updateOrganTransport(Long id, OrganTransportRequest request) {
        OrganTransport transport = organTransportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organ Transport not found with ID: " + id));

        transport.setOrganType(request.getOrganType());
        if (request.getPriority() != null) transport.setPriority(request.getPriority());
        if (request.getEstimatedTimeOfArrival() != null) transport.setEstimatedTimeOfArrival(request.getEstimatedTimeOfArrival());
        if (request.getStatus() != null) transport.setStatus(request.getStatus());
        if (request.getNotes() != null) transport.setNotes(request.getNotes());

        if (request.getAirAmbulanceId() != null) {
            AirAmbulance amb = airAmbulanceRepository.findById(request.getAirAmbulanceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + request.getAirAmbulanceId()));
            transport.setAirAmbulance(amb);
        }

        OrganTransport updated = organTransportRepository.save(transport);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteOrganTransport(Long id) {
        OrganTransport transport = organTransportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organ Transport not found with ID: " + id));
        organTransportRepository.delete(transport);
    }

    @Override
    public OrganTransportResponse getOrganTransportById(Long id) {
        OrganTransport transport = organTransportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organ Transport not found with ID: " + id));
        return mapToResponse(transport);
    }

    @Override
    public List<OrganTransportResponse> getAllOrganTransports() {
        return organTransportRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrganTransportResponse> getOrganTransportsByHospital(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));
        return organTransportRepository.findBySourceHospitalOrDestinationHospital(hospital, hospital).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrganTransportResponse mapToResponse(OrganTransport ot) {
        return OrganTransportResponse.builder()
                .id(ot.getId())
                .organType(ot.getOrganType())
                .sourceHospital(ot.getSourceHospital() != null ? mapHospital(ot.getSourceHospital()) : null)
                .destinationHospital(ot.getDestinationHospital() != null ? mapHospital(ot.getDestinationHospital()) : null)
                .airAmbulance(ot.getAirAmbulance() != null ? mapAmbulance(ot.getAirAmbulance()) : null)
                .priority(ot.getPriority())
                .estimatedTimeOfArrival(ot.getEstimatedTimeOfArrival())
                .status(ot.getStatus())
                .notes(ot.getNotes())
                .createdAt(ot.getCreatedAt())
                .build();
    }

    private com.airambulance.dto.HospitalResponse mapHospital(Hospital h) {
        return com.airambulance.dto.HospitalResponse.builder()
                .id(h.getId())
                .name(h.getName())
                .address(h.getAddress())
                .city(h.getCity())
                .state(h.getState())
                .contactNumber(h.getContactNumber())
                .emergencyContact(h.getEmergencyContact())
                .hasHelipad(h.getHasHelipad())
                .totalICUBeds(h.getTotalICUBeds())
                .availableICUBeds(h.getAvailableICUBeds())
                .userId(h.getUser() != null ? h.getUser().getId() : null)
                .userEmail(h.getUser() != null ? h.getUser().getEmail() : null)
                .build();
    }

    private com.airambulance.dto.AirAmbulanceResponse mapAmbulance(AirAmbulance a) {
        return com.airambulance.dto.AirAmbulanceResponse.builder()
                .id(a.getId())
                .registrationNumber(a.getRegistrationNumber())
                .model(a.getModel())
                .capacity(a.getCapacity())
                .currentLocation(a.getCurrentLocation())
                .fuelStatus(a.getFuelStatus())
                .status(a.getStatus())
                .availability(a.getAvailability())
                .build();
    }
}
