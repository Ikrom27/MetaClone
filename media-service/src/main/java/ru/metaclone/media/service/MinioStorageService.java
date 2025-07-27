package ru.metaclone.media.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.metaclone.media.config.StorageProperties;
import ru.metaclone.media.data.model.PresignedUrl;
import ru.metaclone.media.exceptions.InvalidObjectKeyException;
import ru.metaclone.media.exceptions.NotConfirmedException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MinioStorageService implements IStorageService {

    private final S3Client s3Client;
    private final S3Presigner preSigner;
    private final StorageProperties storageProperties;

    private static final String TEMP_DIR = "temp/";
    private static final String PERMANENT_DIR = "permanent/";

    @Override
    public PresignedUrl generatePreSignedUrl(Long userId, String fileName, String bucketName) {
        String objectKey = userId + "/" + TEMP_DIR + UUID.randomUUID() + "-" + fileName;

        var putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        PresignedPutObjectRequest presignedRequest = preSigner.presignPutObject(builder -> builder
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofSeconds(storageProperties.getSignatureDurationSeconds()))
        );

        OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expiresAt = createdAt.plusSeconds(storageProperties.getSignatureDurationSeconds());

        return new PresignedUrl(
                userId,
                presignedRequest.url().toString(),
                objectKey,
                createdAt,
                expiresAt,
                storageProperties.getSignatureDurationSeconds()
        );
    }

    @Override
    public String generatePublicUrl(Long userId, String objectKey, String bucketName) {
        String publicKey = confirmUpload(userId, objectKey, bucketName);
        return storageProperties.getPublicEndpoint() + "/" + bucketName + "/" + publicKey;
    }

    public String confirmUpload(Long userId, String objectKey, String bucketName) {
        String expectedPrefix = userId + "/" + TEMP_DIR;
        if (!objectKey.startsWith(expectedPrefix)) {
            throw new InvalidObjectKeyException("Object key must start with '" + expectedPrefix + "'");
        }

        String permanentKey = userId + "/" + PERMANENT_DIR + objectKey.substring(expectedPrefix.length());

        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build());
        } catch (Exception e) {
            throw new NotConfirmedException("File not found in temporary storage: " + objectKey);
        }

        s3Client.copyObject(CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(objectKey)
                .destinationBucket(bucketName)
                .destinationKey(permanentKey)
                .build());

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build());

        return permanentKey;
    }
}
