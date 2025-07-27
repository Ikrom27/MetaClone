package ru.metaclone.media.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageProperties {
    @Value("${s3.presignDurationSeconds}")
    private Long PRESIGN_DURATION_SECONDS;

    @Value("${s3.publicEndpoint}")
    private String PUBLIC_ENDPOINT;

    public Long getSignatureDurationSeconds() {
        return PRESIGN_DURATION_SECONDS;
    }

    public String getPublicEndpoint() {
        return PUBLIC_ENDPOINT;
    }
}
