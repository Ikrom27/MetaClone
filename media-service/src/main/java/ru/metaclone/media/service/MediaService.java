package ru.metaclone.media.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.metaclone.media.data.model.PresignedUrl;
import ru.metaclone.media.data.response.AvatarPublishedResponse;
import ru.metaclone.media.data.response.PreSignUrlResponse;
import ru.metaclone.media.events.KafkaProducer;
import ru.metaclone.media.mapper.PresignedUrlMapper;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class MediaService {
    private final KafkaProducer kafkaProducer;
    private final IStorageService storageService;
    private final PresignedUrlMapper presignedUrlMapper;

    public PreSignUrlResponse generatePreSignedUrl(Long userId, String fileName, String bucketName) {
        PresignedUrl presignedUrl = storageService.generatePreSignedUrl(userId, fileName, bucketName);
        return presignedUrlMapper.mapToResponse(presignedUrl);
    }

    public AvatarPublishedResponse publishAvatar(Long userId, String objectKey, String bucketName) {
        String publicUrl = storageService.generatePublicUrl(userId, objectKey, bucketName);
        CompletableFuture.runAsync(() -> kafkaProducer.produceAvatarUpdate(userId, publicUrl));
        return new AvatarPublishedResponse(userId, publicUrl);
    }

    public AvatarPublishedResponse publishImage(Long userId, String objectKey, String bucketName) {
        String publicUrl = storageService.generatePublicUrl(userId, objectKey, bucketName);
        return new AvatarPublishedResponse(userId, publicUrl);
    }
}
