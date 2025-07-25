package ru.metaclone.media.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AvatarPublishedResponse(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("public_url")
        String publicUrl
) { }
