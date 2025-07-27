package ru.metaclone.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.metaclone.users.data.entity.SubscriptionEntity;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, SubscriptionEntity.SubscriptionId> {

    List<SubscriptionEntity> findByIdFolloweeIdOrderByCreatedAtDesc(Long followeeId);

    List<SubscriptionEntity> findByIdFollowerIdOrderByCreatedAtDesc(Long followerId);

    boolean existsByIdFollowerIdAndIdFolloweeId(Long followerId, Long followeeId);
}