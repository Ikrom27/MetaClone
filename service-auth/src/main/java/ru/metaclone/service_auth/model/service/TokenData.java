package ru.metaclone.service_auth.model.service;

import ru.metaclone.service_auth.model.enums.Role;

public record TokenData(Long userId, Role role, Long issuedAt, Long expiredAt) {}

