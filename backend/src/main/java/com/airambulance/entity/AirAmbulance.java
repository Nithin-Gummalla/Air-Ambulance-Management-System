package com.airambulance.entity;

import com.airambulance.enums.AmbulanceStatus;
import com.airambulance.enums.FuelStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "air_ambulances")
public class AirAmbulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private String currentLocation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelStatus fuelStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AmbulanceStatus status;

    private Boolean availability;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pilot_id")
    private Pilot pilot;

    public AirAmbulance() {}

    public AirAmbulance(Long id, String registrationNumber, String model, Integer capacity, String currentLocation, FuelStatus fuelStatus, AmbulanceStatus status, Boolean availability, Pilot pilot) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.model = model;
        this.capacity = capacity;
        this.currentLocation = currentLocation;
        this.fuelStatus = fuelStatus;
        this.status = status;
        this.availability = availability;
        this.pilot = pilot;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }

    public FuelStatus getFuelStatus() { return fuelStatus; }
    public void setFuelStatus(FuelStatus fuelStatus) { this.fuelStatus = fuelStatus; }

    public AmbulanceStatus getStatus() { return status; }
    public void setStatus(AmbulanceStatus status) { this.status = status; }

    public Boolean getAvailability() { return availability; }
    public void setAvailability(Boolean availability) { this.availability = availability; }

    public Pilot getPilot() { return pilot; }
    public void setPilot(Pilot pilot) { this.pilot = pilot; }

    public static AirAmbulanceBuilder builder() { return new AirAmbulanceBuilder(); }

    public static class AirAmbulanceBuilder {
        private Long id;
        private String registrationNumber;
        private String model;
        private Integer capacity;
        private String currentLocation;
        private FuelStatus fuelStatus;
        private AmbulanceStatus status;
        private Boolean availability;
        private Pilot pilot;

        public AirAmbulanceBuilder id(Long id) { this.id = id; return this; }
        public AirAmbulanceBuilder registrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; return this; }
        public AirAmbulanceBuilder model(String model) { this.model = model; return this; }
        public AirAmbulanceBuilder capacity(Integer capacity) { this.capacity = capacity; return this; }
        public AirAmbulanceBuilder currentLocation(String currentLocation) { this.currentLocation = currentLocation; return this; }
        public AirAmbulanceBuilder fuelStatus(FuelStatus fuelStatus) { this.fuelStatus = fuelStatus; return this; }
        public AirAmbulanceBuilder status(AmbulanceStatus status) { this.status = status; return this; }
        public AirAmbulanceBuilder availability(Boolean availability) { this.availability = availability; return this; }
        public AirAmbulanceBuilder pilot(Pilot pilot) { this.pilot = pilot; return this; }

        public AirAmbulance build() {
            return new AirAmbulance(id, registrationNumber, model, capacity, currentLocation, fuelStatus, status, availability, pilot);
        }
    }
}
