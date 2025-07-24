package ru.metaclone.auth.data.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @Valid @NotNull UserCredentials credentials,
        @Valid @NotNull UserDetails userDetails
) { }
