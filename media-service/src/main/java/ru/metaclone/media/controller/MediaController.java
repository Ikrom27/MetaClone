package ru.metaclone.media.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.media.data.response.AvatarPublishedResponse;
import ru.metaclone.media.data.response.PreSignUrlResponse;
import ru.metaclone.media.security.model.Authorities;
import ru.metaclone.media.security.model.UserContext;
import ru.metaclone.media.service.MediaService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/media")
public class MediaController {

    private final MediaService mediaService;

    @Value("${s3.avatarsBucketName}")
    private String AVATARS_BUCKET_NAME;

    @PostMapping("/avatars/presign")
    public ResponseEntity<PreSignUrlResponse> generatePresignUrl(
            @RequestParam("user_id") Long userId,
            @RequestParam("file_name") String fileName,
            @AuthenticationPrincipal UserContext userContext
    ) {
        boolean isOwner = userContext.id().equals(userId);
        boolean isAdmin = userContext.authorities().contains(Authorities.ADMIN);

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Forbidden");
        }
        return ResponseEntity.ok(mediaService.generatePreSignedUrl(userId, fileName, AVATARS_BUCKET_NAME));
    }

    @PostMapping("/avatars/publish")
    public ResponseEntity<AvatarPublishedResponse> publishAvatar(
            @RequestParam("user_id") Long userId,
            @RequestParam("object_key") String objectKey,
            @AuthenticationPrincipal UserContext userContext
    ) {
        boolean isOwner = userContext.id().equals(userId);
        boolean isAdmin = userContext.authorities().contains(Authorities.ADMIN);

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Forbidden");
        }
        return ResponseEntity.ok(mediaService.publishAvatar(userId, objectKey, AVATARS_BUCKET_NAME));
    }
}