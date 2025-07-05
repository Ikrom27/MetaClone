package ru.metaclone.service_auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.service_auth.model.dto.RefreshTokenRequest;
import ru.metaclone.service_auth.model.service.UserCredentials;
import ru.metaclone.service_auth.model.dto.RegisterRequest;
import ru.metaclone.service_auth.model.dto.TokensResponse;
import ru.metaclone.service_auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(ObjectMapper objectMapper, AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> login(@Valid @RequestBody UserCredentials userCredentials) {
        return ResponseEntity.ok(authService.loginUser(userCredentials));
    }

    @PostMapping("/register")
    public ResponseEntity<TokensResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.registerUser(registerRequest.getCredentials()));
    }

    @GetMapping("/refresh_token")
    public ResponseEntity<TokensResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshTokenRequest.refreshToken()));
    }
}
