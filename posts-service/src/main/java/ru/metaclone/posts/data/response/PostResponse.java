package ru.metaclone.posts.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;

public record PostResponse(
        @JsonProperty("post_id")
        Long postId,

        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("content")
        String content,

        @JsonProperty("created_at")
        OffsetDateTime createdAt,

        @JsonProperty("updated_at")
        OffsetDateTime updatedAt,

        @JsonProperty("images")
        List<ImageResponse> images
) { }
