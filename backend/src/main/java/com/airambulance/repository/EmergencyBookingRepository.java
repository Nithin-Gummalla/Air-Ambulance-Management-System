package com.airambulance.repository;

import com.airambulance.entity.AirAmbulance;
import com.airambulance.entity.EmergencyBooking;
import com.airambulance.entity.Hospital;
import com.airambulance.entity.Patient;
import com.airambulance.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmergencyBookingRepository extends JpaRepository<EmergencyBooking, Long> {
    List<EmergencyBooking> findByPatient(Patient patient);
    List<EmergencyBooking> findByDestinationHospital(Hospital hospital);
    List<EmergencyBooking> findByAirAmbulance(AirAmbulance airAmbulance);
    List<EmergencyBooking> findByStatus(BookingStatus status);
    List<EmergencyBooking> findByBookingDate(LocalDate date);
    Long countByStatus(BookingStatus status);
    Long countByBookingDate(LocalDate date);
}
