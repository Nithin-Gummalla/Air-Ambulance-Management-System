package com.airambulance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    private String state;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String emergencyContact;

    private Boolean hasHelipad;

    private Integer totalICUBeds;

    private Integer availableICUBeds;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Hospital() {}

    public Hospital(Long id, String name, String address, String city, String state, String contactNumber, String emergencyContact, Boolean hasHelipad, Integer totalICUBeds, Integer availableICUBeds, User user) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.contactNumber = contactNumber;
        this.emergencyContact = emergencyContact;
        this.hasHelipad = hasHelipad;
        this.totalICUBeds = totalICUBeds;
        this.availableICUBeds = availableICUBeds;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public Boolean getHasHelipad() { return hasHelipad; }
    public void setHasHelipad(Boolean hasHelipad) { this.hasHelipad = hasHelipad; }

    public Integer getTotalICUBeds() { return totalICUBeds; }
    public void setTotalICUBeds(Integer totalICUBeds) { this.totalICUBeds = totalICUBeds; }

    public Integer getAvailableICUBeds() { return availableICUBeds; }
    public void setAvailableICUBeds(Integer availableICUBeds) { this.availableICUBeds = availableICUBeds; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static HospitalBuilder builder() { return new HospitalBuilder(); }

    public static class HospitalBuilder {
        private Long id;
        private String name;
        private String address;
        private String city;
        private String state;
        private String contactNumber;
        private String emergencyContact;
        private Boolean hasHelipad;
        private Integer totalICUBeds;
        private Integer availableICUBeds;
        private User user;

        public HospitalBuilder id(Long id) { this.id = id; return this; }
        public HospitalBuilder name(String name) { this.name = name; return this; }
        public HospitalBuilder address(String address) { this.address = address; return this; }
        public HospitalBuilder city(String city) { this.city = city; return this; }
        public HospitalBuilder state(String state) { this.state = state; return this; }
        public HospitalBuilder contactNumber(String contactNumber) { this.contactNumber = contactNumber; return this; }
        public HospitalBuilder emergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; return this; }
        public HospitalBuilder hasHelipad(Boolean hasHelipad) { this.hasHelipad = hasHelipad; return this; }
        public HospitalBuilder totalICUBeds(Integer totalICUBeds) { this.totalICUBeds = totalICUBeds; return this; }
        public HospitalBuilder availableICUBeds(Integer availableICUBeds) { this.availableICUBeds = availableICUBeds; return this; }
        public HospitalBuilder user(User user) { this.user = user; return this; }

        public Hospital build() {
            return new Hospital(id, name, address, city, state, contactNumber, emergencyContact, hasHelipad, totalICUBeds, availableICUBeds, user);
        }
    }
}
