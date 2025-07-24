package ru.metaclone.media.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.metaclone.media.data.model.PresignedUrl;
import ru.metaclone.media.data.response.PreSignUrlResponse;
import ru.metaclone.media.events.KafkaProducer;
import ru.metaclone.media.mapper.PresignedUrlMapper;

@Service
@AllArgsConstructor
public class MediaService {
    private final KafkaProducer kafkaProducer;
    private final IStorageService storageService;
    private final PresignedUrlMapper presignedUrlMapper;

    public PreSignUrlResponse generatePreSignedUrl(Long userId, String fileName) {
        PresignedUrl presignedUrl = storageService.generatePreSignedUrl(userId, fileName);
        return presignedUrlMapper.mapToResponse(presignedUrl);
    }

    public void confirmUpload(Long userId, String objectKey) {
        String publicUrl = storageService.generatePublicUrl(userId, objectKey);
        kafkaProducer.produceAvatarUpdate(userId, publicUrl);
    }
}
