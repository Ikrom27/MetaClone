package ru.metaclone.service_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.metaclone.service_auth.model.entity.TokenEntity;

@Repository
public interface TokensRepository extends JpaRepository<TokenEntity, Long> { }
