package ru.metaclone.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCredentials(
        @NotBlank
        @Size(min = 4, max = 50)
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "login incorrect symbols")
        String login,

        @NotBlank
        @Size(min = 6, max = 100)
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+=-]+$", message = "password incorrect symbols")
        String password
) { }
