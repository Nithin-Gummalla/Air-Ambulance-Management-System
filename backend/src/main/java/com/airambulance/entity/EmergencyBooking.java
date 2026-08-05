package com.airambulance.entity;

import com.airambulance.enums.BookingStatus;
import com.airambulance.enums.EmergencyLevel;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_bookings")
public class EmergencyBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private String pickupLocation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_hospital_id", nullable = false)
    private Hospital destinationHospital;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "air_ambulance_id")
    private AirAmbulance airAmbulance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmergencyLevel emergencyLevel;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private LocalTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    private String notes;

    private LocalDateTime createdAt;

    public EmergencyBooking() {}

    public EmergencyBooking(Long id, Patient patient, String pickupLocation, Hospital destinationHospital, AirAmbulance airAmbulance, EmergencyLevel emergencyLevel, LocalDate bookingDate, LocalTime bookingTime, BookingStatus status, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.patient = patient;
        this.pickupLocation = pickupLocation;
        this.destinationHospital = destinationHospital;
        this.airAmbulance = airAmbulance;
        this.emergencyLevel = emergencyLevel;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public Hospital getDestinationHospital() { return destinationHospital; }
    public void setDestinationHospital(Hospital destinationHospital) { this.destinationHospital = destinationHospital; }

    public AirAmbulance getAirAmbulance() { return airAmbulance; }
    public void setAirAmbulance(AirAmbulance airAmbulance) { this.airAmbulance = airAmbulance; }

    public EmergencyLevel getEmergencyLevel() { return emergencyLevel; }
    public void setEmergencyLevel(EmergencyLevel emergencyLevel) { this.emergencyLevel = emergencyLevel; }

    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }

    public LocalTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static EmergencyBookingBuilder builder() { return new EmergencyBookingBuilder(); }

    public static class EmergencyBookingBuilder {
        private Long id;
        private Patient patient;
        private String pickupLocation;
        private Hospital destinationHospital;
        private AirAmbulance airAmbulance;
        private EmergencyLevel emergencyLevel;
        private LocalDate bookingDate;
        private LocalTime bookingTime;
        private BookingStatus status;
        private String notes;
        private LocalDateTime createdAt;

        public EmergencyBookingBuilder id(Long id) { this.id = id; return this; }
        public EmergencyBookingBuilder patient(Patient patient) { this.patient = patient; return this; }
        public EmergencyBookingBuilder pickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; return this; }
        public EmergencyBookingBuilder destinationHospital(Hospital destinationHospital) { this.destinationHospital = destinationHospital; return this; }
        public EmergencyBookingBuilder airAmbulance(AirAmbulance airAmbulance) { this.airAmbulance = airAmbulance; return this; }
        public EmergencyBookingBuilder emergencyLevel(EmergencyLevel emergencyLevel) { this.emergencyLevel = emergencyLevel; return this; }
        public EmergencyBookingBuilder bookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; return this; }
        public EmergencyBookingBuilder bookingTime(LocalTime bookingTime) { this.bookingTime = bookingTime; return this; }
        public EmergencyBookingBuilder status(BookingStatus status) { this.status = status; return this; }
        public EmergencyBookingBuilder notes(String notes) { this.notes = notes; return this; }
        public EmergencyBookingBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public EmergencyBooking build() {
            return new EmergencyBooking(id, patient, pickupLocation, destinationHospital, airAmbulance, emergencyLevel, bookingDate, bookingTime, status, notes, createdAt);
        }
    }
}
