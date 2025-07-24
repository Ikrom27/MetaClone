package ru.metaclone.auth.data.service;

import java.util.List;
import java.util.UUID;

public record TokenData(UUID tokenId, Long userId, List<String> authorities, Long issuedAt, Long expiresAt) {}

