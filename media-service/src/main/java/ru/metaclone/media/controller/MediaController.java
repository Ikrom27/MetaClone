package ru.metaclone.media.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.metaclone.media.data.response.PreSignUrlResponse;
import ru.metaclone.media.service.MediaService;
import ru.metaclone.security.model.Authorities;
import ru.metaclone.security.model.UserContext;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/media")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/presign")
    public PreSignUrlResponse generatePresignUrl(
            @RequestParam("user_id") Long userId,
            @RequestParam("file_name") String fileName
    ) {
        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();
        UserContext userContext = (UserContext) auth.getPrincipal();
        boolean isOwner = userContext.id().equals(userId);
        boolean isAdmin = userContext.authorities().contains(Authorities.ADMIN);

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Forbidden");
        }
        return mediaService.generatePreSignedUrl(userId, fileName);
    }

//    @PostMapping("/publish")
//    public void publishAvatar(
//            @RequestParam("object_key") String objectKey
//    ) {
//        mediaService.publishAvatar(user.getId(), objectKey);
//    }
}