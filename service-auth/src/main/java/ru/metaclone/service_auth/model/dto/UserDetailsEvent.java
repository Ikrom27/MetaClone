package ru.metaclone.service_auth.model.dto;

import ru.metaclone.service_auth.model.enums.Gender;

public record UserDetailsEvent(String firstName, String lastName, Long birthday, Gender gender) { }
