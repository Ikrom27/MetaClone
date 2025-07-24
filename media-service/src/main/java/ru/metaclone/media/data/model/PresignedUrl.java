package ru.metaclone.media.data.model;

import java.time.OffsetDateTime;

public record PresignedUrl (
        Long userId,
        String url,
        String objectKey,
        OffsetDateTime createdAt,
        OffsetDateTime expiresAt,
        Long durationInSeconds
) { }
