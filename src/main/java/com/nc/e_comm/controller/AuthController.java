package com.nc.e_comm.controller;

import com.nc.e_comm.dto.AuthRequestDto;
import com.nc.e_comm.dto.AuthResponseDto;
import com.nc.e_comm.dto.RegisterRequestDto;
import com.nc.e_comm.dto.TokenRefreshRequest;
import com.nc.e_comm.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto req) {
        AuthResponseDto resp = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto req){
        AuthResponseDto resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("refresh")
    public ResponseEntity<AuthResponseDto> refresh(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        AuthResponseDto resp = authService.refreshToken(tokenRefreshRequest.getRefreshToken());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody TokenRefreshRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

}
