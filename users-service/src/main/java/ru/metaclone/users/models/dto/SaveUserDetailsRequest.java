package ru.metaclone.users.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.metaclone.users.models.enums.Gender;

import java.time.LocalDate;

public record SaveUserDetailsRequest(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("login")
        String login,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        @JsonProperty("birthday")
        LocalDate birthday,

        @JsonProperty("gender")
        Gender gender
) { }
