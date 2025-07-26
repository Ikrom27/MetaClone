package ru.metaclone.users.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record UserResponse(
        @JsonProperty("user_id")
        Long userId,

        @JsonProperty("login")
        String login,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("second_name")
        String secondName,

        @JsonProperty("avatar_url")
        String avatarUrl,

        @JsonProperty("about")
        String about,

        @JsonProperty("birthday")
        OffsetDateTime birthday,

        @JsonProperty("gender")
        String gender,

        @JsonProperty("follows_count")
        Integer followsCount,

        @JsonProperty("followers_count")
        Integer followersCount
) {}