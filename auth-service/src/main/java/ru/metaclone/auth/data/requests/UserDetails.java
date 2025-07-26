package ru.metaclone.auth.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ru.metaclone.auth.data.enums.Gender;

import java.time.OffsetDateTime;

public record UserDetails(
        @NotNull
        @NotBlank
        @JsonProperty("first_name")
        String firstName,

        @NotNull
        @NotBlank
        @JsonProperty("last_name")
        String lastName,

        @NotNull
        @PastOrPresent(message = "birthday must be in the past or present")
        @JsonProperty("birthday")
        OffsetDateTime birthday,

        @NotNull
        @JsonProperty("gender")
        Gender gender
) {}