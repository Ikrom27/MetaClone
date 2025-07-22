package ru.metaclone.auth.model.dto;

import ru.metaclone.auth.model.enums.Gender;

import java.time.LocalDate;

public record UserDetailsEvent(Long userId, String login, String firstName, String lastName, LocalDate birthday, Gender gender) { }
