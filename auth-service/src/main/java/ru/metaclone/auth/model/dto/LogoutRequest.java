package ru.metaclone.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record LogoutRequest(
        @NotNull
        @JsonProperty("refresh_token")
        String refreshToken
) {}
