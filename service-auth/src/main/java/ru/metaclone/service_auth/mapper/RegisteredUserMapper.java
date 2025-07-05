package ru.metaclone.service_auth.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.metaclone.service_auth.model.service.UserCredentials;
import ru.metaclone.service_auth.model.entity.RegisteredUserEntity;

import java.util.UUID;

@Component
public class RegisteredUserMapper {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public RegisteredUserEntity mapRegisteredUserEntity(UserCredentials credentials) {
        var salt = generateUserSalt();
        var password = passwordEncoder.encode(credentials.getPassword() + salt);
        return new RegisteredUserEntity(credentials.getLogin(), password, salt);
    }

    private String generateUserSalt() {
        return UUID.randomUUID().toString();
    }
}
