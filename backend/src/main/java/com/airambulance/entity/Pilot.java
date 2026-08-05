package com.airambulance.entity;

import com.airambulance.enums.PilotStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "pilots")
public class Pilot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private Integer experienceYears;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PilotStatus status;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Pilot() {}

    public Pilot(Long id, String name, String licenseNumber, Integer experienceYears, String phone, PilotStatus status, User user) {
        this.id = id;
        this.name = name;
        this.licenseNumber = licenseNumber;
        this.experienceYears = experienceYears;
        this.phone = phone;
        this.status = status;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public Integer getExperienceYears() { return experienceYears; }
    public void setExperienceYears(Integer experienceYears) { this.experienceYears = experienceYears; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public PilotStatus getStatus() { return status; }
    public void setStatus(PilotStatus status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static PilotBuilder builder() { return new PilotBuilder(); }

    public static class PilotBuilder {
        private Long id;
        private String name;
        private String licenseNumber;
        private Integer experienceYears;
        private String phone;
        private PilotStatus status;
        private User user;

        public PilotBuilder id(Long id) { this.id = id; return this; }
        public PilotBuilder name(String name) { this.name = name; return this; }
        public PilotBuilder licenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; return this; }
        public PilotBuilder experienceYears(Integer experienceYears) { this.experienceYears = experienceYears; return this; }
        public PilotBuilder phone(String phone) { this.phone = phone; return this; }
        public PilotBuilder status(PilotStatus status) { this.status = status; return this; }
        public PilotBuilder user(User user) { this.user = user; return this; }

        public Pilot build() {
            return new Pilot(id, name, licenseNumber, experienceYears, phone, status, user);
        }
    }
}
