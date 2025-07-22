package ru.metaclone.service_auth.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LogoutResponse(
        @JsonProperty("message")
        String message
) { }
