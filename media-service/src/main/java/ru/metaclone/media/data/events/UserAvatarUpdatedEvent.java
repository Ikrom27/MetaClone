package ru.metaclone.media.data.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserAvatarUpdatedEvent(
        @JsonProperty("user_id")
        Long userId,
        @JsonProperty("avatar_url")
        String avatarUrl
) { }
