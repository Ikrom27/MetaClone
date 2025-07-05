package ru.metaclone.service_auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.metaclone.service_auth.exception.InvalidTokenException;
import ru.metaclone.service_auth.exception.UserAlreadyExistException;
import ru.metaclone.service_auth.exception.UserNotFountException;
import ru.metaclone.service_auth.mapper.RegisteredUserMapper;
import ru.metaclone.service_auth.model.entity.TokenEntity;
import ru.metaclone.service_auth.model.enums.Role;
import ru.metaclone.service_auth.model.service.TokenData;
import ru.metaclone.service_auth.model.service.UserCredentials;
import ru.metaclone.service_auth.repository.AuthRepository;
import ru.metaclone.service_auth.model.dto.TokensResponse;
import ru.metaclone.service_auth.repository.TokensRepository;

@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final TokensRepository tokensRepository;
    private final RegisteredUserMapper registeredUserMapper;
    private final JwtService jwtService;
    private final TokenDataFactory tokenDataFactory;

    private static final String USER_ALREADY_EXIST = "User with this login already exist";
    private static final String USER_NOT_FOUND = "User with this login not found";

    @Autowired
    public AuthService(
            AuthRepository authRepository,
            TokensRepository tokensRepository,
            RegisteredUserMapper registeredUserMapper,
            JwtService jwtService,
            TokenDataFactory tokenDataFactory
    ) {
        this.authRepository = authRepository;
        this.tokensRepository = tokensRepository;
        this.registeredUserMapper = registeredUserMapper;
        this.jwtService = jwtService;
        this.tokenDataFactory = tokenDataFactory;
    }

    public TokensResponse loginUser(UserCredentials credentials) throws UserNotFountException {
        var user = authRepository.findByLogin(credentials.getLogin());
        if (user == null) {
            throw new UserNotFountException(USER_NOT_FOUND);
        }

        TokenData accessTokenData = tokenDataFactory.createAccessToken(user.getUserId(), Role.AUTHORISED_USER);
        TokenData refreshTokenData = tokenDataFactory.createAccessToken(user.getUserId(), Role.AUTHORISED_USER);
        String accessJwt = jwtService.generateAccessToken(accessTokenData);
        String refreshJwt = jwtService.generateRefreshToken(refreshTokenData);

        tokensRepository.save(new TokenEntity(refreshTokenData.userId(), refreshJwt,
                        refreshTokenData.issuedAt(), refreshTokenData.expiredAt()));

        return new TokensResponse(accessJwt, refreshJwt);
    }

    public TokensResponse registerUser(UserCredentials userCredentials) throws UserAlreadyExistException {
        if (isUserExist(userCredentials.getLogin())) {
            throw new UserAlreadyExistException(USER_ALREADY_EXIST);
        }

        var registeredUser = registeredUserMapper.mapRegisteredUserEntity(userCredentials);
        authRepository.save(registeredUser);

        TokenData accessTokenData = tokenDataFactory.createAccessToken(registeredUser.getUserId(), Role.AUTHORISED_USER);
        TokenData refreshTokenData = tokenDataFactory.createAccessToken(registeredUser.getUserId(), Role.AUTHORISED_USER);
        String accessJwt = jwtService.generateAccessToken(accessTokenData);
        String refreshJwt = jwtService.generateRefreshToken(refreshTokenData);

        tokensRepository.save(
                new TokenEntity(
                        refreshTokenData.userId(),
                        refreshJwt,
                        refreshTokenData.issuedAt(),
                        refreshTokenData.expiredAt()
                )
        );

        return new TokensResponse(accessJwt, refreshJwt);
    }

    public TokensResponse refreshAccessToken(String refreshToken) throws InvalidTokenException {
        Long userId = jwtService.getRefreshTokenUserId(refreshToken);

        String accessToken = jwtService.generateAccessToken(
                tokenDataFactory.createAccessToken(userId, Role.AUTHORISED_USER)
        );

        return new TokensResponse(accessToken, refreshToken);
    }

    private boolean isUserExist(String login) {
        return authRepository.findByLogin(login) != null;
    }
}
