package com.airambulance.entity;

import com.airambulance.enums.OrganPriority;
import com.airambulance.enums.OrganTransportStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "organ_transports")
public class OrganTransport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String organType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_hospital_id", nullable = false)
    private Hospital sourceHospital;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_hospital_id", nullable = false)
    private Hospital destinationHospital;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "air_ambulance_id")
    private AirAmbulance airAmbulance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganPriority priority;

    private String estimatedTimeOfArrival;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrganTransportStatus status;

    private String notes;

    private LocalDateTime createdAt;

    public OrganTransport() {}

    public OrganTransport(Long id, String organType, Hospital sourceHospital, Hospital destinationHospital, AirAmbulance airAmbulance, OrganPriority priority, String estimatedTimeOfArrival, OrganTransportStatus status, String notes, LocalDateTime createdAt) {
        this.id = id;
        this.organType = organType;
        this.sourceHospital = sourceHospital;
        this.destinationHospital = destinationHospital;
        this.airAmbulance = airAmbulance;
        this.priority = priority;
        this.estimatedTimeOfArrival = estimatedTimeOfArrival;
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

    public String getOrganType() { return organType; }
    public void setOrganType(String organType) { this.organType = organType; }

    public Hospital getSourceHospital() { return sourceHospital; }
    public void setSourceHospital(Hospital sourceHospital) { this.sourceHospital = sourceHospital; }

    public Hospital getDestinationHospital() { return destinationHospital; }
    public void setDestinationHospital(Hospital destinationHospital) { this.destinationHospital = destinationHospital; }

    public AirAmbulance getAirAmbulance() { return airAmbulance; }
    public void setAirAmbulance(AirAmbulance airAmbulance) { this.airAmbulance = airAmbulance; }

    public OrganPriority getPriority() { return priority; }
    public void setPriority(OrganPriority priority) { this.priority = priority; }

    public String getEstimatedTimeOfArrival() { return estimatedTimeOfArrival; }
    public void setEstimatedTimeOfArrival(String estimatedTimeOfArrival) { this.estimatedTimeOfArrival = estimatedTimeOfArrival; }

    public OrganTransportStatus getStatus() { return status; }
    public void setStatus(OrganTransportStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static OrganTransportBuilder builder() { return new OrganTransportBuilder(); }

    public static class OrganTransportBuilder {
        private Long id;
        private String organType;
        private Hospital sourceHospital;
        private Hospital destinationHospital;
        private AirAmbulance airAmbulance;
        private OrganPriority priority;
        private String estimatedTimeOfArrival;
        private OrganTransportStatus status;
        private String notes;
        private LocalDateTime createdAt;

        public OrganTransportBuilder id(Long id) { this.id = id; return this; }
        public OrganTransportBuilder organType(String organType) { this.organType = organType; return this; }
        public OrganTransportBuilder sourceHospital(Hospital sourceHospital) { this.sourceHospital = sourceHospital; return this; }
        public OrganTransportBuilder destinationHospital(Hospital destinationHospital) { this.destinationHospital = destinationHospital; return this; }
        public OrganTransportBuilder airAmbulance(AirAmbulance airAmbulance) { this.airAmbulance = airAmbulance; return this; }
        public OrganTransportBuilder priority(OrganPriority priority) { this.priority = priority; return this; }
        public OrganTransportBuilder estimatedTimeOfArrival(String estimatedTimeOfArrival) { this.estimatedTimeOfArrival = estimatedTimeOfArrival; return this; }
        public OrganTransportBuilder status(OrganTransportStatus status) { this.status = status; return this; }
        public OrganTransportBuilder notes(String notes) { this.notes = notes; return this; }
        public OrganTransportBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public OrganTransport build() {
            return new OrganTransport(id, organType, sourceHospital, destinationHospital, airAmbulance, priority, estimatedTimeOfArrival, status, notes, createdAt);
        }
    }
}
