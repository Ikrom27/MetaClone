package ru.metaclone.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.metaclone.users.data.entity.UserEntity;
import ru.metaclone.users.data.response.UserPreview;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> {
    @Query("""
        SELECT new ru.metaclone.users.data.response.UserPreview(
            u.userId, u.firstName, u.lastName, u.login, u.avatarUrl
        )
        FROM UserEntity u
        WHERE u.userId IN :userIds
    """)
    List<UserPreview> findUserPreviewsByUserIds(@Param("userIds") List<Long> userIds, Pageable pageable);
}
