package ru.metaclone.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.metaclone.users.data.entity.UserEntity;

@Repository
public interface UsersRepository extends JpaRepository<UserEntity, Long> { }
