package com.airambulance.service;

import com.airambulance.config.JwtUtils;
import com.airambulance.config.UserDetailsImpl;
import com.airambulance.dto.*;
import com.airambulance.entity.*;
import com.airambulance.enums.PilotStatus;
import com.airambulance.enums.RoleName;
import com.airambulance.exception.BadRequestException;
import com.airambulance.exception.ResourceNotFoundException;
import com.airambulance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HospitalRepository hospitalRepository;
    private final PilotRepository pilotRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        String refreshToken = jwtUtils.generateRefreshTokenFromUsername(loginRequest.getEmail());

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Long hospitalId = hospitalRepository.findByUserId(userDetails.getId()).map(Hospital::getId).orElse(null);
        Long pilotId = pilotRepository.findByUserId(userDetails.getId()).map(Pilot::getId).orElse(null);
        Long patientId = patientRepository.findByUserId(userDetails.getId()).map(Patient::getId).orElse(null);

        return JwtResponse.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .type("Bearer")
                .id(userDetails.getId())
                .name(userDetails.getName())
                .email(userDetails.getEmail())
                .roles(roles)
                .hospitalId(hospitalId)
                .pilotId(pilotId)
                .patientId(patientId)
                .build();
    }

    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already in use!");
        }

        User user = User.builder()
                .name(registerRequest.getName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .build();

        Set<Role> roles = new HashSet<>();
        RoleName reqRole = registerRequest.getRole() != null ? registerRequest.getRole() : RoleName.ROLE_PATIENT;
        Role role = roleRepository.findByName(reqRole)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));
        roles.add(role);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        // Auto-create associated profile based on role
        if (reqRole == RoleName.ROLE_HOSPITAL) {
            Hospital hospital = Hospital.builder()
                    .name(registerRequest.getName())
                    .address(registerRequest.getAddress() != null ? registerRequest.getAddress() : "Default Address")
                    .city(registerRequest.getCity() != null ? registerRequest.getCity() : "City")
                    .contactNumber(registerRequest.getPhone() != null ? registerRequest.getPhone() : "N/A")
                    .emergencyContact(registerRequest.getEmergencyContact() != null ? registerRequest.getEmergencyContact() : "N/A")
                    .hasHelipad(true)
                    .totalICUBeds(20)
                    .availableICUBeds(5)
                    .user(savedUser)
                    .build();
            hospitalRepository.save(hospital);
        } else if (reqRole == RoleName.ROLE_PILOT) {
            Pilot pilot = Pilot.builder()
                    .name(registerRequest.getName())
                    .licenseNumber(registerRequest.getLicenseNumber() != null ? registerRequest.getLicenseNumber() : "LIC-" + System.currentTimeMillis())
                    .experienceYears(registerRequest.getExperienceYears() != null ? registerRequest.getExperienceYears() : 5)
                    .phone(registerRequest.getPhone() != null ? registerRequest.getPhone() : "N/A")
                    .status(PilotStatus.AVAILABLE)
                    .user(savedUser)
                    .build();
            pilotRepository.save(pilot);
        } else if (reqRole == RoleName.ROLE_PATIENT) {
            Patient patient = Patient.builder()
                    .name(registerRequest.getName())
                    .age(registerRequest.getAge() != null ? registerRequest.getAge() : 30)
                    .gender(registerRequest.getGender() != null ? registerRequest.getGender() : "Not Specified")
                    .bloodGroup(registerRequest.getBloodGroup() != null ? registerRequest.getBloodGroup() : "O+")
                    .emergencyContact(registerRequest.getEmergencyContact() != null ? registerRequest.getEmergencyContact() : registerRequest.getPhone())
                    .user(savedUser)
                    .build();
            patientRepository.save(patient);
        }

        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .roles(savedUser.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String token = refreshTokenRequest.getRefreshToken();
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));

            String newToken = jwtUtils.generateTokenFromUsername(username);
            List<String> roles = user.getRoles().stream()
                    .map(r -> r.getName().name())
                    .collect(Collectors.toList());

            Long hospitalId = hospitalRepository.findByUserId(user.getId()).map(Hospital::getId).orElse(null);
            Long pilotId = pilotRepository.findByUserId(user.getId()).map(Pilot::getId).orElse(null);
            Long patientId = patientRepository.findByUserId(user.getId()).map(Patient::getId).orElse(null);

            return JwtResponse.builder()
                    .token(newToken)
                    .refreshToken(token)
                    .type("Bearer")
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .roles(roles)
                    .hospitalId(hospitalId)
                    .pilotId(pilotId)
                    .patientId(patientId)
                    .build();
        } else {
            throw new BadRequestException("Invalid refresh token");
        }
    }

    @Override
    public UserResponse getCurrentUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toList()))
                .build();
    }
}
