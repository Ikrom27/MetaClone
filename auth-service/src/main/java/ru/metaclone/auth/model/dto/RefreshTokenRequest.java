package ru.metaclone.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(
        @NotNull
        @JsonProperty("refresh_token")
        String refreshToken
) {}
