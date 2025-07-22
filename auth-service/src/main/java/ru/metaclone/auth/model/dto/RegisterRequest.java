package ru.metaclone.auth.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @Valid @NotNull UserCredentials credentials,
        @Valid @NotNull UserDetails userDetails
) { }
