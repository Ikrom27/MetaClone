package ru.metaclone.posts.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserPostsResponse(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("posts")
        List<PostResponse> posts
) { }
