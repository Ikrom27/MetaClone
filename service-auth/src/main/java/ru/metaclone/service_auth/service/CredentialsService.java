package ru.metaclone.service_auth.service;

import org.springframework.stereotype.Service;
import ru.metaclone.service_auth.mapper.RegisteredUserEntityMapper;
import ru.metaclone.service_auth.model.dto.UserCredentials;
import ru.metaclone.service_auth.repository.AuthRepository;

@Service
public class CredentialsService {
    private final AuthRepository authRepository;
    private final RegisteredUserEntityMapper registeredUserEntityMapper;

    public CredentialsService(AuthRepository authRepository,
                              RegisteredUserEntityMapper registeredUserEntityMapper) {
        this.authRepository = authRepository;
        this.registeredUserEntityMapper = registeredUserEntityMapper;
    }

    public Long getUserIdByLogin(String login) {
        return authRepository.findByLogin(login).getUserId();
    }

    public boolean isUserExistWithLogin(String login) {
        return authRepository.findByLogin(login) != null;
    }

    public Long saveUserCredential(UserCredentials userCredentials) {
        var credentialsEntity = registeredUserEntityMapper.mapUserCredentials(userCredentials);
        authRepository.save(credentialsEntity);
        return credentialsEntity.getUserId();
    }
}
