package ru.metaclone.service_auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import ru.metaclone.service_auth.model.enums.Gender;

import java.time.LocalDate;

public record UserDetails(
        @NotNull
        @NotBlank String firstName,

        @NotNull
        @NotBlank String lastName,

        @NotNull
        @PastOrPresent(message = "birthday must be in the past or present")
        LocalDate birthday,

        @NotNull Gender gender
) {}