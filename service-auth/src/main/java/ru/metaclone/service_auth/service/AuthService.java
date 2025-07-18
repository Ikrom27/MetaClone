package ru.metaclone.service_auth.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import ru.metaclone.service_auth.exception.InvalidTokenException;
import ru.metaclone.service_auth.exception.UserAlreadyExistException;
import ru.metaclone.service_auth.exception.UserNotFountException;
import ru.metaclone.service_auth.mapper.UserDetailsEventMapper;
import ru.metaclone.service_auth.model.dto.UserCredentials;
import ru.metaclone.service_auth.model.dto.UserDetails;
import ru.metaclone.service_auth.model.dto.TokensResponse;

@Service
public class AuthService {
    private final TokensService tokensService;
    private final CredentialsService credentialsService;
    private final UserDetailsEventMapper userDetailsEventMapper;
    private final UsersDetailsProducer usersDetailsProducer;

    private static final String USER_ALREADY_EXIST = "User with this login already exist";
    private static final String USER_NOT_FOUND = "User with this login not found";

    public AuthService(
            TokensService tokensService,
            CredentialsService credentialsService,
            UsersDetailsProducer usersDetailsProducer,
            UserDetailsEventMapper userDetailsEventMapper
    ) {
        this.credentialsService = credentialsService;
        this.tokensService = tokensService;
        this.usersDetailsProducer = usersDetailsProducer;
        this.userDetailsEventMapper = userDetailsEventMapper;
    }

    public TokensResponse loginUser(UserCredentials credentials) throws UserNotFountException {
        var userId = credentialsService.getUserIdByLogin(credentials.login());
        if (userId == null) {
            throw new UserNotFountException(USER_NOT_FOUND);
        }
        return tokensService.generateAndSaveTokens(userId);
    }

    public TokensResponse registerUser(
            @NotNull UserCredentials userCredentials,
            @NotNull UserDetails userDetails) throws UserAlreadyExistException {
        if (credentialsService.isUserExistWithLogin(userCredentials.login())) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }
        var userId = credentialsService.saveUserCredential(userCredentials);
        usersDetailsProducer.sendUserInfo(userDetailsEventMapper.mapUserDetailsEvent(userDetails));
        return tokensService.generateAndSaveTokens(userId);
    }

    public TokensResponse refreshAccessToken(String refreshToken) throws InvalidTokenException {
        return tokensService.refreshAccessToken(refreshToken);
    }
}
