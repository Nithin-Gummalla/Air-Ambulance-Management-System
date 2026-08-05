package com.airambulance.dto;

import java.util.List;

public class JwtResponse {

    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private Long id;
    private String name;
    private String email;
    private List<String> roles;
    private Long hospitalId;
    private Long pilotId;
    private Long patientId;

    public JwtResponse() {}

    public JwtResponse(String token, String refreshToken, String type, Long id, String name, String email, List<String> roles, Long hospitalId, Long pilotId, Long patientId) {
        this.token = token;
        this.refreshToken = refreshToken;
        if (type != null) this.type = type;
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.hospitalId = hospitalId;
        this.pilotId = pilotId;
        this.patientId = patientId;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public Long getHospitalId() { return hospitalId; }
    public void setHospitalId(Long hospitalId) { this.hospitalId = hospitalId; }

    public Long getPilotId() { return pilotId; }
    public void setPilotId(Long pilotId) { this.pilotId = pilotId; }

    public Long getPatientId() { return patientId; }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public static JwtResponseBuilder builder() { return new JwtResponseBuilder(); }

    public static class JwtResponseBuilder {
        private String token;
        private String refreshToken;
        private String type = "Bearer";
        private Long id;
        private String name;
        private String email;
        private List<String> roles;
        private Long hospitalId;
        private Long pilotId;
        private Long patientId;

        public JwtResponseBuilder token(String token) { this.token = token; return this; }
        public JwtResponseBuilder refreshToken(String refreshToken) { this.refreshToken = refreshToken; return this; }
        public JwtResponseBuilder type(String type) { this.type = type; return this; }
        public JwtResponseBuilder id(Long id) { this.id = id; return this; }
        public JwtResponseBuilder name(String name) { this.name = name; return this; }
        public JwtResponseBuilder email(String email) { this.email = email; return this; }
        public JwtResponseBuilder roles(List<String> roles) { this.roles = roles; return this; }
        public JwtResponseBuilder hospitalId(Long hospitalId) { this.hospitalId = hospitalId; return this; }
        public JwtResponseBuilder pilotId(Long pilotId) { this.pilotId = pilotId; return this; }
        public JwtResponseBuilder patientId(Long patientId) { this.patientId = patientId; return this; }

        public JwtResponse build() {
            return new JwtResponse(token, refreshToken, type, id, name, email, roles, hospitalId, pilotId, patientId);
        }
    }
}
