package ru.metaclone.media.service;

import ru.metaclone.media.data.model.PresignedUrl;

public interface IStorageService {
    PresignedUrl generatePreSignedUrl(Long userId, String objectKey, String bucketName);
    String generatePublicUrl(Long userId, String objectKey, String bucketName);
}
