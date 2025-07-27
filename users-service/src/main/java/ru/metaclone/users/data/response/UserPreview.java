package ru.metaclone.users.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserPreview {
    @JsonProperty("user_id")
    Long userId;
    @JsonProperty("first_name")
    String firstName;
    @JsonProperty("second_name")
    String lastName;
    @JsonProperty("login")
    String login;
    @JsonProperty("avatar_url")
    String avatarUrl;
}
