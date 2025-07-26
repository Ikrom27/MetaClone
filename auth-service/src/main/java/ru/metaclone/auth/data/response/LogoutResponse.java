package ru.metaclone.auth.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LogoutResponse(
        @JsonProperty("message")
        String message
) { }
