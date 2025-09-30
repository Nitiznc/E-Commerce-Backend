package com.nc.e_comm.service;

import com.nc.e_comm.dto.AuthRequestDto;
import com.nc.e_comm.dto.AuthResponseDto;
import com.nc.e_comm.dto.RegisterRequestDto;
import com.nc.e_comm.exception.BadRequestException;
import com.nc.e_comm.exception.TokenNotFoundException;
import com.nc.e_comm.model.RefreshToken;
import com.nc.e_comm.model.Role;
import com.nc.e_comm.model.User;
import com.nc.e_comm.repository.RoleRepository;
import com.nc.e_comm.repository.UserRepository;
import com.nc.e_comm.security.jwt.JwtProvider;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponseDto register(RegisterRequestDto req) {
        // 1. Check if email already exists
        if (userRepository.existsByEmail(req.getEmail())){
            throw new BadRequestException("Email Already in use");
        }

        // 2. Validate password and confirm password
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        Role userRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("ROLE_USER is not found"));

        User user = User.builder()
                .name(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .enabled(true)
                .roles(new HashSet<>())
                .build();
        user.getRoles().add(userRole);

        userRepository.save(user);

        String accessToken = jwtProvider.createAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(jwtProvider.getAccessTokenMs())
                .build();
    }

    public AuthResponseDto login(AuthRequestDto req){
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Invalid email or password");
        }

        String accessToken = jwtProvider.createAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponseDto(accessToken, refreshToken.getToken(), jwtProvider.getAccessTokenMs());
    }

    @Transactional
    public AuthResponseDto refreshToken(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenString)
                .orElseThrow(()-> new TokenNotFoundException("Refresh token not found"));

        refreshTokenService.revoke(refreshToken);

        User user = refreshToken.getUser();
        RefreshToken newRefresh = refreshTokenService.createRefreshToken(user);
        String accessToken = jwtProvider.createAccessToken(user);
        return new AuthResponseDto(accessToken, newRefresh.getToken(), jwtProvider.getAccessTokenMs());
    }

    public void logout(String refreshTokenString){
        refreshTokenService.findByToken(refreshTokenString)
                .ifPresent(refreshTokenService::revoke);
    }

}
