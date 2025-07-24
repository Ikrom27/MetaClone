package ru.metaclone.auth.model.dto;

import ru.metaclone.auth.model.enums.Gender;

import java.time.OffsetDateTime;

public record UserDetailsEvent(
        Long userId,
        String login,
        String firstName,
        String lastName,
        OffsetDateTime birthday,
        Gender gender
) { }
