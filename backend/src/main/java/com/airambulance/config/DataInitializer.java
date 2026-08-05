package com.airambulance.config;

import com.airambulance.entity.*;
import com.airambulance.enums.*;
import com.airambulance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    private final PilotRepository pilotRepository;
    private final AirAmbulanceRepository airAmbulanceRepository;
    private final PatientRepository patientRepository;
    private final EmergencyBookingRepository emergencyBookingRepository;
    private final OrganTransportRepository organTransportRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize Roles
        Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build()));
        Role hospitalRole = roleRepository.findByName(RoleName.ROLE_HOSPITAL)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_HOSPITAL).build()));
        Role pilotRole = roleRepository.findByName(RoleName.ROLE_PILOT)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_PILOT).build()));
        Role patientRole = roleRepository.findByName(RoleName.ROLE_PATIENT)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ROLE_PATIENT).build()));

        // Create Admin User if not exists
        if (!userRepository.existsByEmail("admin@airambulance.com")) {
            User admin = User.builder()
                    .name("System Administrator")
                    .email("admin@airambulance.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("+1-800-555-0199")
                    .roles(Set.of(adminRole))
                    .build();
            userRepository.save(admin);
        }

        // Create Hospital User & Hospital Profile
        Hospital hospital1 = null;
        Hospital hospital2 = null;
        if (!userRepository.existsByEmail("cityhospital@med.org")) {
            User hUser1 = User.builder()
                    .name("City General Hospital")
                    .email("cityhospital@med.org")
                    .password(passwordEncoder.encode("hospital123"))
                    .phone("+1-555-0144")
                    .roles(Set.of(hospitalRole))
                    .build();
            hUser1 = userRepository.save(hUser1);

            hospital1 = Hospital.builder()
                    .name("City General Trauma Center")
                    .address("100 Medical Center Way")
                    .city("New York")
                    .state("NY")
                    .contactNumber("+1-555-0144")
                    .emergencyContact("+1-555-9111")
                    .hasHelipad(true)
                    .totalICUBeds(50)
                    .availableICUBeds(12)
                    .user(hUser1)
                    .build();
            hospital1 = hospitalRepository.save(hospital1);

            User hUser2 = User.builder()
                    .name("St. Jude Cardiac Institute")
                    .email("stjude@med.org")
                    .password(passwordEncoder.encode("hospital123"))
                    .phone("+1-555-0188")
                    .roles(Set.of(hospitalRole))
                    .build();
            hUser2 = userRepository.save(hUser2);

            hospital2 = Hospital.builder()
                    .name("St. Jude Cardiac & Surgical Center")
                    .address("450 Healthcare Blvd")
                    .city("Boston")
                    .state("MA")
                    .contactNumber("+1-555-0188")
                    .emergencyContact("+1-555-9222")
                    .hasHelipad(true)
                    .totalICUBeds(40)
                    .availableICUBeds(8)
                    .user(hUser2)
                    .build();
            hospital2 = hospitalRepository.save(hospital2);
        } else {
            hospital1 = hospitalRepository.findAll().stream().findFirst().orElse(null);
            hospital2 = hospitalRepository.findAll().stream().skip(1).findFirst().orElse(hospital1);
        }

        // Create Pilot User & Pilot Profile
        Pilot pilot1 = null;
        if (!userRepository.existsByEmail("captain.john@skyrescue.com")) {
            User pUser1 = User.builder()
                    .name("Captain John Miller")
                    .email("captain.john@skyrescue.com")
                    .password(passwordEncoder.encode("pilot123"))
                    .phone("+1-555-0177")
                    .roles(Set.of(pilotRole))
                    .build();
            pUser1 = userRepository.save(pUser1);

            pilot1 = Pilot.builder()
                    .name("Captain John Miller")
                    .licenseNumber("AV-987654")
                    .experienceYears(12)
                    .phone("+1-555-0177")
                    .status(PilotStatus.AVAILABLE)
                    .user(pUser1)
                    .build();
            pilot1 = pilotRepository.save(pilot1);
        } else {
            pilot1 = pilotRepository.findAll().stream().findFirst().orElse(null);
        }

        // Create Air Ambulance Fleet
        AirAmbulance ambulance1 = null;
        if (airAmbulanceRepository.count() == 0) {
            ambulance1 = AirAmbulance.builder()
                    .registrationNumber("MED-EVAC-01")
                    .model("Eurocopter EC135 Medical Spec")
                    .capacity(4)
                    .currentLocation("JFK International Helipad, NY")
                    .fuelStatus(FuelStatus.FULL)
                    .status(AmbulanceStatus.AVAILABLE)
                    .availability(true)
                    .pilot(pilot1)
                    .build();
            ambulance1 = airAmbulanceRepository.save(ambulance1);

            AirAmbulance ambulance2 = AirAmbulance.builder()
                    .registrationNumber("MED-EVAC-02")
                    .model("Bell 429 GlobalRanger ICU")
                    .capacity(6)
                    .currentLocation("Logan Airport Helibase, Boston")
                    .fuelStatus(FuelStatus.HIGH)
                    .status(AmbulanceStatus.AVAILABLE)
                    .availability(true)
                    .build();
            airAmbulanceRepository.save(ambulance2);
        } else {
            ambulance1 = airAmbulanceRepository.findAll().stream().findFirst().orElse(null);
        }

        // Create Patient User & Patient Profile
        Patient patient1 = null;
        if (!userRepository.existsByEmail("patient.smith@gmail.com")) {
            User patUser = User.builder()
                    .name("Robert Smith")
                    .email("patient.smith@gmail.com")
                    .password(passwordEncoder.encode("patient123"))
                    .phone("+1-555-0133")
                    .roles(Set.of(patientRole))
                    .build();
            patUser = userRepository.save(patUser);

            patient1 = Patient.builder()
                    .name("Robert Smith")
                    .age(45)
                    .gender("Male")
                    .bloodGroup("O+")
                    .emergencyContact("+1-555-9988")
                    .medicalCondition("Acute Coronary Syndrome / Cardiac Emergency")
                    .user(patUser)
                    .build();
            patient1 = patientRepository.save(patient1);
        } else {
            patient1 = patientRepository.findAll().stream().findFirst().orElse(null);
        }

        // Create Sample Emergency Booking
        if (emergencyBookingRepository.count() == 0 && patient1 != null && hospital1 != null) {
            EmergencyBooking booking = EmergencyBooking.builder()
                    .patient(patient1)
                    .pickupLocation("Springfield Regional Airfield")
                    .destinationHospital(hospital1)
                    .airAmbulance(ambulance1)
                    .emergencyLevel(EmergencyLevel.CRITICAL)
                    .bookingDate(LocalDate.now())
                    .bookingTime(LocalTime.now())
                    .status(BookingStatus.ASSIGNED)
                    .notes("Immediate airlift required due to cardiac complication.")
                    .build();
            emergencyBookingRepository.save(booking);
        }

        // Create Sample Organ Transport
        if (organTransportRepository.count() == 0 && hospital1 != null && hospital2 != null) {
            OrganTransport organTransport = OrganTransport.builder()
                    .organType("Heart")
                    .sourceHospital(hospital1)
                    .destinationHospital(hospital2)
                    .airAmbulance(ambulance1)
                    .priority(OrganPriority.CRITICAL)
                    .estimatedTimeOfArrival("45 Mins")
                    .status(OrganTransportStatus.IN_TRANSIT)
                    .notes("Donor heart transport in temperature-controlled cooler.")
                    .build();
            organTransportRepository.save(organTransport);
        }

        // Create Sample System Notifications
        if (notificationRepository.count() == 0) {
            Notification n1 = Notification.builder()
                    .title("Emergency Transport Dispatched")
                    .message("Air Ambulance MED-EVAC-01 has been assigned to Patient Robert Smith.")
                    .type(NotificationType.FLIGHT_ASSIGNED)
                    .isRead(false)
                    .build();
            notificationRepository.save(n1);

            Notification n2 = Notification.builder()
                    .title("Organ Transport Scheduled")
                    .message("Heart transport scheduled from City General Trauma Center to St. Jude Institute.")
                    .type(NotificationType.ORGAN_TRANSPORT_ALERT)
                    .isRead(false)
                    .build();
            notificationRepository.save(n2);
        }
    }
}
