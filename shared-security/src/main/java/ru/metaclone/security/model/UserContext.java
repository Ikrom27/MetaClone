package ru.metaclone.security.model;

import java.util.List;

public record UserContext(Long id, List<String> authorities) {}
