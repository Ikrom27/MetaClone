package ru.metaclone.media.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.media.data.model.PresignedUrl;
import ru.metaclone.media.data.response.PreSignUrlResponse;

@Component
public class PresignedUrlMapper {
    public PreSignUrlResponse mapToResponse(PresignedUrl presignedUrl) {
        return new PreSignUrlResponse(presignedUrl.userId(), presignedUrl.url(), presignedUrl.objectKey() ,presignedUrl.createdAt(),
                presignedUrl.expiresAt(), presignedUrl.durationInSeconds());
    }
}
