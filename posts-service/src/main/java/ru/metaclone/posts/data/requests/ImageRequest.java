package ru.metaclone.posts.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ImageRequest(
        @JsonProperty("image_url")
        String imageUrl
) { }
