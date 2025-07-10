package ru.metaclone.service_auth.model.service;

import ru.metaclone.service_auth.model.enums.Gender;

public record UserInfo(String firstName, String lastName, Long birthday, Gender gender) {}