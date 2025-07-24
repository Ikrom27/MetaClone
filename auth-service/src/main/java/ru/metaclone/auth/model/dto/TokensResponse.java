package ru.metaclone.auth.model.dto;

public record TokensResponse(
        Long userId,
        String accessToken,
        String refreshToken
) {}
