package ru.metaclone.service_auth.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TokenEntity {
    @Id
    @Column(name = "user_id", unique = true, updatable = false, nullable = false)
    private Long userId;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "created_at", nullable = false)
    private Long createdAt;

    @Column(name = "expired_at", nullable = false)
    private Long expiredAt;
}
