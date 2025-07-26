package ru.metaclone.auth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.auth.data.requests.LogoutRequest;
import ru.metaclone.auth.data.response.LogoutResponse;
import ru.metaclone.auth.data.requests.RefreshTokenRequest;
import ru.metaclone.auth.data.requests.UserCredentials;
import ru.metaclone.auth.data.requests.RegisterRequest;
import ru.metaclone.auth.data.response.TokensResponse;
import ru.metaclone.auth.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokensResponse> login(@Valid @RequestBody UserCredentials userCredentials) {
        return ResponseEntity.ok(authService.loginUser(userCredentials, getAuthorities()));
    }

    @PostMapping("/register")
    public ResponseEntity<TokensResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        var body = authService
                .registerUser(registerRequest.credentials(), registerRequest.userDetails(), getAuthorities());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<TokensResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshTokenRequest.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        return ResponseEntity.ok(authService.logout(logoutRequest.refreshToken()));
    }

    private List<String> getAuthorities(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
}
