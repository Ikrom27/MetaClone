package ru.metaclone.media.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImagePublishedResponse(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("public_url")
        String publicUrl
) { }
