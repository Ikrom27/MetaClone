package ru.metaclone.users.models.events;

import ru.metaclone.users.models.enums.Gender;

import java.time.LocalDate;

public record UserCreatedEvent(String userId, String login, String firstName,
                               String lastName, LocalDate birthday, Gender gender) { }
