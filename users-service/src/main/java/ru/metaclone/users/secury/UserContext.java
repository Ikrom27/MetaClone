package ru.metaclone.users.secury;

import java.util.List;

public record UserContext(Long id, List<String> authorities) {}
