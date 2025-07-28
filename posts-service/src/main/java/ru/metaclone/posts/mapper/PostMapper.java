package ru.metaclone.posts.mapper;

import org.springframework.stereotype.Service;
import ru.metaclone.posts.data.entity.ImageEntity;
import ru.metaclone.posts.data.entity.PostEntity;
import ru.metaclone.posts.data.requests.PostRequest;
import ru.metaclone.posts.data.response.ImageResponse;
import ru.metaclone.posts.data.response.PostResponse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostMapper {

    public PostEntity toEntity(PostRequest request, Long userId) {
        PostEntity post = PostEntity.builder()
                .userId(userId)
                .content(request.content())
                .createdAt(OffsetDateTime.now())
                .updatedAt(null)
                .build();

        if (request.images() != null && !request.images().isEmpty()) {
            List<ImageEntity> images = request.images().stream()
                    .map(imageRequest -> ImageEntity.builder()
                            .url(imageRequest.imageUrl())
                            .post(post)
                            .build())
                    .toList();

            post.setImages(images);
        }

        return post;
    }

    public PostResponse toResponse(PostEntity postEntity) {
        return new PostResponse(
                postEntity.getPostId(),
                postEntity.getUserId(),
                postEntity.getContent(),
                postEntity.getCreatedAt(),
                postEntity.getUpdatedAt(),
                mapImages(postEntity.getImages())
        );
    }

    private List<ImageResponse> mapImages(List<ImageEntity> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        return images.stream()
                .map(image -> new ImageResponse(
                        image.getImageId(),
                        image.getUrl()
                ))
                .collect(Collectors.toList());
    }
}