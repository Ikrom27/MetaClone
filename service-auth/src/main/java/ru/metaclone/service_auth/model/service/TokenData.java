package ru.metaclone.service_auth.model.service;

import java.util.List;
import java.util.UUID;

public record TokenData(UUID tokenId, Long userId, List<String> authorities, Long issuedAt, Long expiresAt) {}

