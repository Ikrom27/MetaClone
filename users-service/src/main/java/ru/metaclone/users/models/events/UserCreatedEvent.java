package ru.metaclone.users.models.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.metaclone.users.models.enums.Gender;

import java.time.LocalDate;

public record UserCreatedEvent(
        String userId,
        String login,
        String firstName,
        String lastName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthday,
        Gender gender
) { }
