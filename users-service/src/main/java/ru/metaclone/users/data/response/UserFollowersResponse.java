package ru.metaclone.users.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserFollowersResponse(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("followers")
        List<UserPreview> followers
) {}
