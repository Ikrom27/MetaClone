package ru.metaclone.media.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record PreSignUrlResponse(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("presign_url")
        String presignUrl,

        @JsonProperty("object_key")
        String objectKey,

        @JsonProperty("created_at")
        OffsetDateTime createdAt,

        @JsonProperty("expires_at")
        OffsetDateTime expiresAt,

        @JsonProperty("duration_at_seconds")
        Long durationAtSeconds
) { }
