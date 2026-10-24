package com.airambulance.service;

import com.airambulance.dto.EmergencyBookingRequest;
import com.airambulance.dto.EmergencyBookingResponse;
import com.airambulance.dto.UpdateBookingStatusRequest;
import com.airambulance.entity.*;
import com.airambulance.enums.AmbulanceStatus;
import com.airambulance.enums.BookingStatus;
import com.airambulance.enums.FuelStatus;
import com.airambulance.enums.NotificationType;
import com.airambulance.enums.PilotStatus;
import com.airambulance.exception.BadRequestException;
import com.airambulance.exception.ResourceNotFoundException;
import com.airambulance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyBookingServiceImpl implements EmergencyBookingService {

    private final EmergencyBookingRepository bookingRepository;
    private final PatientRepository patientRepository;
    private final HospitalRepository hospitalRepository;
    private final AirAmbulanceRepository airAmbulanceRepository;
    private final PilotRepository pilotRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public EmergencyBookingResponse createBooking(EmergencyBookingRequest request, String currentUserEmail) {
        Patient patient = null;

        if (request.getPatientId() != null) {
            patient = patientRepository.findById(request.getPatientId())
                    .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + request.getPatientId()));
        } else if (currentUserEmail != null) {
            Optional<User> userOpt = userRepository.findByEmail(currentUserEmail);
            if (userOpt.isPresent()) {
                patient = patientRepository.findByUser(userOpt.get()).orElse(null);
            }
        }

        if (patient == null) {
            String pName = request.getPatientName() != null ? request.getPatientName() : "Emergency Patient";
            patient = Patient.builder()
                    .name(pName)
                    .age(request.getPatientAge() != null ? request.getPatientAge() : 30)
                    .gender(request.getPatientGender() != null ? request.getPatientGender() : "Not Specified")
                    .bloodGroup(request.getPatientBloodGroup() != null ? request.getPatientBloodGroup() : "O+")
                    .emergencyContact(request.getEmergencyContact() != null ? request.getEmergencyContact() : "N/A")
                    .medicalCondition(request.getMedicalCondition() != null ? request.getMedicalCondition() : "Critical Emergency")
                    .build();
            patient = patientRepository.save(patient);
        }

        Hospital hospital = hospitalRepository.findById(request.getDestinationHospitalId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination Hospital not found with ID: " + request.getDestinationHospitalId()));

        AirAmbulance assignedAmbulance = null;
        BookingStatus status = BookingStatus.REQUESTED;

        if (request.getAirAmbulanceId() != null) {
            assignedAmbulance = airAmbulanceRepository.findById(request.getAirAmbulanceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + request.getAirAmbulanceId()));
            if (!isSuitableForEmergency(assignedAmbulance, request.getEmergencyLevel())) {
                throw new BadRequestException("Selected air ambulance is unavailable or does not meet the fuel and pilot requirements for this emergency level");
            }
            assignedAmbulance.setAvailability(false);
            assignedAmbulance.setStatus(AmbulanceStatus.IN_SERVICE);
            airAmbulanceRepository.save(assignedAmbulance);
            if (assignedAmbulance.getPilot() != null) {
                assignedAmbulance.getPilot().setStatus(PilotStatus.ON_MISSION);
                pilotRepository.save(assignedAmbulance.getPilot());
            }
            status = BookingStatus.ASSIGNED;
        } else {
            assignedAmbulance = airAmbulanceRepository.findByAvailability(true).stream()
                    .filter(ambulance -> isSuitableForEmergency(ambulance, request.getEmergencyLevel()))
                    .findFirst()
                    .orElse(null);
            if (assignedAmbulance != null) {
                assignedAmbulance.setAvailability(false);
                assignedAmbulance.setStatus(AmbulanceStatus.IN_SERVICE);
                airAmbulanceRepository.save(assignedAmbulance);
                if (assignedAmbulance.getPilot() != null) {
                    assignedAmbulance.getPilot().setStatus(PilotStatus.ON_MISSION);
                    pilotRepository.save(assignedAmbulance.getPilot());
                }
                status = BookingStatus.ASSIGNED;
            }
        }

        EmergencyBooking booking = EmergencyBooking.builder()
                .patient(patient)
                .pickupLocation(request.getPickupLocation())
                .destinationHospital(hospital)
                .airAmbulance(assignedAmbulance)
                .emergencyLevel(request.getEmergencyLevel())
                .bookingDate(request.getBookingDate() != null ? request.getBookingDate() : LocalDate.now())
                .bookingTime(request.getBookingTime() != null ? request.getBookingTime() : LocalTime.now())
                .status(status)
                .notes(request.getNotes())
                .build();

        EmergencyBooking saved = bookingRepository.save(booking);

        sendNotificationsForBooking(saved);

        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public EmergencyBookingResponse updateBookingStatus(Long id, UpdateBookingStatusRequest statusRequest) {
        EmergencyBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency Booking not found with ID: " + id));

        BookingStatus newStatus = statusRequest.getStatus();

        if (statusRequest.getAirAmbulanceId() != null
                && newStatus != BookingStatus.COMPLETED
                && newStatus != BookingStatus.CANCELLED) {
            AirAmbulance amb = airAmbulanceRepository.findById(statusRequest.getAirAmbulanceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + statusRequest.getAirAmbulanceId()));
            boolean sameAmbulance = booking.getAirAmbulance() != null
                    && booking.getAirAmbulance().getId().equals(amb.getId());
            if (!sameAmbulance && !isSuitableForEmergency(amb, booking.getEmergencyLevel())) {
                throw new BadRequestException("Selected air ambulance is unavailable or does not meet the fuel and pilot requirements for this emergency level");
            }
            if (!sameAmbulance && booking.getAirAmbulance() != null) {
                releaseAmbulance(booking.getAirAmbulance());
            }
            booking.setAirAmbulance(amb);
            amb.setAvailability(false);
            amb.setStatus(AmbulanceStatus.IN_SERVICE);
            if (amb.getPilot() != null) {
                amb.getPilot().setStatus(PilotStatus.ON_MISSION);
                pilotRepository.save(amb.getPilot());
            }
            airAmbulanceRepository.save(amb);
        }

        booking.setStatus(newStatus);
        if (newStatus == BookingStatus.COMPLETED || newStatus == BookingStatus.CANCELLED) {
            if (booking.getAirAmbulance() != null) {
                releaseAmbulance(booking.getAirAmbulance());
            }
        }

        EmergencyBooking updated = bookingRepository.save(booking);

        Notification notification = Notification.builder()
                .title("Booking Status Updated: " + newStatus.name())
                .message("Booking #" + booking.getId() + " for patient " + booking.getPatient().getName() + " is now " + newStatus.name())
                .type(newStatus == BookingStatus.COMPLETED ? NotificationType.FLIGHT_COMPLETED : NotificationType.EMERGENCY_ALERT)
                .recipientUser(booking.getDestinationHospital().getUser())
                .isRead(false)
                .build();
        notificationRepository.save(notification);

        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public EmergencyBookingResponse assignAirAmbulance(Long bookingId, Long airAmbulanceId) {
        EmergencyBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency Booking not found with ID: " + bookingId));
        AirAmbulance amb = airAmbulanceRepository.findById(airAmbulanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Air Ambulance not found with ID: " + airAmbulanceId));

        if (!isSuitableForEmergency(amb, booking.getEmergencyLevel())
                && (booking.getAirAmbulance() == null || !booking.getAirAmbulance().getId().equals(amb.getId()))) {
            throw new BadRequestException("Selected air ambulance is not available for assignment");
        }

        AirAmbulance previousAmbulance = booking.getAirAmbulance();
        if (previousAmbulance != null && !previousAmbulance.getId().equals(amb.getId())) {
            previousAmbulance.setAvailability(true);
            previousAmbulance.setStatus(AmbulanceStatus.AVAILABLE);
            if (previousAmbulance.getPilot() != null) {
                previousAmbulance.getPilot().setStatus(PilotStatus.AVAILABLE);
                pilotRepository.save(previousAmbulance.getPilot());
            }
            airAmbulanceRepository.save(previousAmbulance);
        }

        booking.setAirAmbulance(amb);
        booking.setStatus(BookingStatus.ASSIGNED);
        amb.setAvailability(false);
        amb.setStatus(AmbulanceStatus.IN_SERVICE);
        if (amb.getPilot() != null) {
            amb.getPilot().setStatus(PilotStatus.ON_MISSION);
        }
        airAmbulanceRepository.save(amb);

        sendNotificationsForBooking(booking);

        return mapToResponse(bookingRepository.save(booking));
    }

    @Override
    public EmergencyBookingResponse getBookingById(Long id) {
        EmergencyBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency Booking not found with ID: " + id));
        return mapToResponse(booking);
    }

    @Override
    public List<EmergencyBookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyBookingResponse> getBookingsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + patientId));
        return bookingRepository.findByPatient(patient).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyBookingResponse> getBookingsByHospital(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));
        return bookingRepository.findByDestinationHospital(hospital).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmergencyBookingResponse> getBookingsByPilot(Long pilotId) {
        Pilot pilot = pilotRepository.findById(pilotId)
                .orElseThrow(() -> new ResourceNotFoundException("Pilot not found with ID: " + pilotId));
        Optional<AirAmbulance> amb = airAmbulanceRepository.findByPilot(pilot);
        if (amb.isEmpty()) {
            return List.of();
        }
        return bookingRepository.findByAirAmbulance(amb.get()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        EmergencyBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emergency Booking not found with ID: " + id));
        booking.setStatus(BookingStatus.CANCELLED);
        if (booking.getAirAmbulance() != null) {
            releaseAmbulance(booking.getAirAmbulance());
        }
        bookingRepository.save(booking);
    }

    /**
     * An aircraft is suitable only when its availability, operational status,
     * pilot duty status, and fuel level meet the emergency's urgency.
     * CRITICAL/HIGH requests require HIGH or FULL fuel; MODERATE/LOW requests
     * require at least MEDIUM fuel.
     */
    private void releaseAmbulance(AirAmbulance ambulance) {
        ambulance.setAvailability(true);
        ambulance.setStatus(AmbulanceStatus.AVAILABLE);
        if (ambulance.getPilot() != null) {
            ambulance.getPilot().setStatus(PilotStatus.AVAILABLE);
            pilotRepository.save(ambulance.getPilot());
        }
        airAmbulanceRepository.save(ambulance);
    }

    private boolean isSuitableForEmergency(AirAmbulance ambulance, com.airambulance.enums.EmergencyLevel emergencyLevel) {
        if (!Boolean.TRUE.equals(ambulance.getAvailability())
                || ambulance.getStatus() != AmbulanceStatus.AVAILABLE
                || (ambulance.getPilot() != null && ambulance.getPilot().getStatus() != PilotStatus.AVAILABLE)) {
            return false;
        }

        if (ambulance.getFuelStatus() == null || emergencyLevel == null) {
            return false;
        }

        return switch (emergencyLevel) {
            case CRITICAL, HIGH -> ambulance.getFuelStatus() == FuelStatus.FULL
                    || ambulance.getFuelStatus() == FuelStatus.HIGH;
            case MODERATE, LOW -> ambulance.getFuelStatus() == FuelStatus.FULL
                    || ambulance.getFuelStatus() == FuelStatus.HIGH
                    || ambulance.getFuelStatus() == FuelStatus.MEDIUM;
        };
    }

    private void sendNotificationsForBooking(EmergencyBooking booking) {
        Notification nHospital = Notification.builder()
                .title("New Emergency Airlift Request")
                .message("Inbound patient " + booking.getPatient().getName() + " scheduled for " + booking.getDestinationHospital().getName() + ". Level: " + booking.getEmergencyLevel())
                .type(NotificationType.BOOKING_CREATED)
                .recipientUser(booking.getDestinationHospital().getUser())
                .isRead(false)
                .build();
        notificationRepository.save(nHospital);

        if (booking.getAirAmbulance() != null && booking.getAirAmbulance().getPilot() != null) {
            Notification nPilot = Notification.builder()
                    .title("Flight Assignment Dispatch")
                    .message("Mission assigned on " + booking.getAirAmbulance().getRegistrationNumber() + ". Pickup: " + booking.getPickupLocation())
                    .type(NotificationType.FLIGHT_ASSIGNED)
                    .recipientUser(booking.getAirAmbulance().getPilot().getUser())
                    .isRead(false)
                    .build();
            notificationRepository.save(nPilot);
        }
    }

    private EmergencyBookingResponse mapToResponse(EmergencyBooking b) {
        return EmergencyBookingResponse.builder()
                .id(b.getId())
                .patient(b.getPatient() != null ? mapPatient(b.getPatient()) : null)
                .pickupLocation(b.getPickupLocation())
                .destinationHospital(b.getDestinationHospital() != null ? mapHospital(b.getDestinationHospital()) : null)
                .airAmbulance(b.getAirAmbulance() != null ? mapAmbulance(b.getAirAmbulance()) : null)
                .emergencyLevel(b.getEmergencyLevel())
                .bookingDate(b.getBookingDate())
                .bookingTime(b.getBookingTime())
                .status(b.getStatus())
                .notes(b.getNotes())
                .createdAt(b.getCreatedAt())
                .build();
    }

    private com.airambulance.dto.PatientResponse mapPatient(Patient p) {
        return com.airambulance.dto.PatientResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .age(p.getAge())
                .gender(p.getGender())
                .bloodGroup(p.getBloodGroup())
                .emergencyContact(p.getEmergencyContact())
                .medicalCondition(p.getMedicalCondition())
                .userId(p.getUser() != null ? p.getUser().getId() : null)
                .userEmail(p.getUser() != null ? p.getUser().getEmail() : null)
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
