package ru.metaclone.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.metaclone.auth.data.entity.CredentialsEntity;

public interface AuthRepository extends JpaRepository<CredentialsEntity, Long> {
    CredentialsEntity findByLogin(String login);
}
