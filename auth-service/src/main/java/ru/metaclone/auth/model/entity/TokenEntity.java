package ru.metaclone.auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class TokenEntity {
    @Id
    @Column(name = "token_id", unique = true, updatable = false, nullable = false)
    private UUID tokenId;

    @Column(name = "user_id", updatable = false, nullable = false)
    private Long userId;

    @Column(name = "refresh_token", columnDefinition = "TEXT", nullable = false)
    private String refreshToken;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;
}
