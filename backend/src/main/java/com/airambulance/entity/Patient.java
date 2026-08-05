package com.airambulance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String bloodGroup;

    @Column(nullable = false)
    private String emergencyContact;

    @Column(columnDefinition = "TEXT")
    private String medicalCondition;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Patient() {}

    public Patient(Long id, String name, Integer age, String gender, String bloodGroup, String emergencyContact, String medicalCondition, User user) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.emergencyContact = emergencyContact;
        this.medicalCondition = medicalCondition;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getMedicalCondition() { return medicalCondition; }
    public void setMedicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public static PatientBuilder builder() { return new PatientBuilder(); }

    public static class PatientBuilder {
        private Long id;
        private String name;
        private Integer age;
        private String gender;
        private String bloodGroup;
        private String emergencyContact;
        private String medicalCondition;
        private User user;

        public PatientBuilder id(Long id) { this.id = id; return this; }
        public PatientBuilder name(String name) { this.name = name; return this; }
        public PatientBuilder age(Integer age) { this.age = age; return this; }
        public PatientBuilder gender(String gender) { this.gender = gender; return this; }
        public PatientBuilder bloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; return this; }
        public PatientBuilder emergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; return this; }
        public PatientBuilder medicalCondition(String medicalCondition) { this.medicalCondition = medicalCondition; return this; }
        public PatientBuilder user(User user) { this.user = user; return this; }

        public Patient build() {
            return new Patient(id, name, age, gender, bloodGroup, emergencyContact, medicalCondition, user);
        }
    }
}
