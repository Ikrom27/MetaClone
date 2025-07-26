package ru.metaclone.users.data.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.metaclone.users.data.enums.Gender;

import java.time.OffsetDateTime;

public record UserCreatedEvent(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("login")
        String login,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("birthday")
        OffsetDateTime birthday,

        @JsonProperty("gender")
        Gender gender
) { }
