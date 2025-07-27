package ru.metaclone.users.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "subscription",
        indexes = {
                @Index(name = "idx_follower_id", columnList = "follower_id"),
                @Index(name = "idx_followee_id", columnList = "followee_id")
            }
        )
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SubscriptionEntity {
    @EmbeddedId
    private SubscriptionId id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class SubscriptionId implements Serializable {

        @Column(name = "follower_id", nullable = false)
        private Long followerId;

        @Column(name = "followee_id", nullable = false)
        private Long followeeId;
    }
}