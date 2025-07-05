package ru.metaclone.service_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.metaclone.service_auth.model.entity.RegisteredUserEntity;

public interface AuthRepository extends JpaRepository<RegisteredUserEntity, Long> {
    RegisteredUserEntity findByLogin(String login);
}
