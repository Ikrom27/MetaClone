package ru.metaclone.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LogoutResponse(
        @JsonProperty("message")
        String message
) { }
