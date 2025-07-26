package ru.metaclone.users.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.metaclone.users.data.enums.Gender;

import java.time.OffsetDateTime;

public record UpdateUserRequest(
        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("about")
        String about,

        @JsonProperty("birthday")
        OffsetDateTime birthday,

        @JsonProperty("gender")
        Gender gender
) { }
