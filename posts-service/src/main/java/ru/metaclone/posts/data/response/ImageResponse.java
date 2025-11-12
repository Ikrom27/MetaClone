package ru.metaclone.posts.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageResponse(
        @JsonProperty("image_id")
        Long imageId,

        @JsonProperty("image_url")
        String imageUrl
) { }
