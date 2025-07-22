package ru.metaclone.users.models.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserAvatarUpdatedEvent(
        @JsonProperty("user_id")
        Long userId,
        @JsonProperty("avatar_url")
        String avatarUrl
) { }
