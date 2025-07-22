package ru.metaclone.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.metaclone.auth.model.entity.TokenEntity;

import java.util.UUID;

@Repository
public interface TokensRepository extends JpaRepository<TokenEntity, UUID> { }
