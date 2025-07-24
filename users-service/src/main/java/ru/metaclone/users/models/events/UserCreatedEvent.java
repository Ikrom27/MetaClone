package ru.metaclone.users.models.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.metaclone.users.models.enums.Gender;

import java.time.OffsetDateTime;

public record UserCreatedEvent(
        Long userId,
        String login,
        String firstName,
        String lastName,
        OffsetDateTime birthday,
        Gender gender
) { }
