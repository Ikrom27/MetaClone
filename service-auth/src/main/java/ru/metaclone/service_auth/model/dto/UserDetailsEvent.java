package ru.metaclone.service_auth.model.dto;

import ru.metaclone.service_auth.model.enums.Gender;

import java.time.LocalDate;

public record UserDetailsEvent(String login, String firstName, String lastName, LocalDate birthday, Gender gender) { }
