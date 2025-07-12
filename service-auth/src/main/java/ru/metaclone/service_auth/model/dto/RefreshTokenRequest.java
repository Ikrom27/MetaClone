package ru.metaclone.service_auth.model.dto;

import jakarta.validation.constraints.NotNull;

public record RefreshTokenRequest(@NotNull String refreshToken) {}
