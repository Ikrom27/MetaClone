package ru.metaclone.auth.data.response;

public record TokensResponse(
        Long userId,
        String accessToken,
        String refreshToken
) {}
