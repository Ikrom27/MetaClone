package ru.metaclone.posts.data.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PostRequest(
        @JsonProperty("content")
        String content,

        @JsonProperty("images")
        List<ImageRequest> images
) {
}
