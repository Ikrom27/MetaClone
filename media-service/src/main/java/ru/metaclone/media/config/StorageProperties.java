package ru.metaclone.media.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StorageProperties {
    @Value("${s3.avatarsBucketName}")
    private String BUCKET_NAME;

    @Value("${s3.presignDurationSeconds}")
    private Long PRESIGN_DURATION_SECONDS;

    @Value("${s3.basePublicUrl}")
    private String BASE_PUBLIC_URL;

    public String getBucketName() {
        return BUCKET_NAME;
    }

    public Long getSignatureDurationSeconds() {
        return PRESIGN_DURATION_SECONDS;
    }

    public String getPublicBaseUrl() {
        return BASE_PUBLIC_URL;
    }
}
