package ru.metaclone.auth.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.metaclone.auth.model.dto.UserCredentials;
import ru.metaclone.auth.model.entity.CredentialsEntity;

import java.util.UUID;

@Component
public class RegisteredUserEntityMapper {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CredentialsEntity mapUserCredentials(UserCredentials credentials) {
        var salt = generateUserSalt();
        var password = passwordEncoder.encode(credentials.password() + salt);
        return new CredentialsEntity(credentials.login(), password, salt);
    }

    private String generateUserSalt() {
        return UUID.randomUUID().toString();
    }
}
