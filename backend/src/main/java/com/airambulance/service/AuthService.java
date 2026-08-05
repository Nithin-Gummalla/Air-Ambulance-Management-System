package com.airambulance.service;

import com.airambulance.dto.JwtResponse;
import com.airambulance.dto.LoginRequest;
import com.airambulance.dto.RefreshTokenRequest;
import com.airambulance.dto.RegisterRequest;
import com.airambulance.dto.UserResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    UserResponse registerUser(RegisterRequest registerRequest);
    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    UserResponse getCurrentUserProfile(String email);
}
