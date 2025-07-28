package ru.metaclone.search.data.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.metaclone.search.data.enums.Gender;

import java.time.OffsetDateTime;

public record UserUpdatedEvent(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("login")
        String login,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("avatar_url")
        String avatarUrl,

        @JsonProperty("about")
        String about,

        @JsonProperty("birthday")
        OffsetDateTime birthday,

        @JsonProperty("gender")
        Gender gender
) {}
