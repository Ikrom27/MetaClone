package ru.metaclone.service_auth.model.dto;

import ru.metaclone.service_auth.model.enums.Gender;

public record UserInfoEvent(String firstName, String lastName, Long birthday, Gender gender) { }
