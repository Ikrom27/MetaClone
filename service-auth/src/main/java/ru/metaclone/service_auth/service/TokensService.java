package ru.metaclone.service_auth.service;

import org.springframework.stereotype.Service;
import ru.metaclone.service_auth.exception.InvalidTokenException;
import ru.metaclone.service_auth.model.dto.TokensResponse;
import ru.metaclone.service_auth.model.entity.TokenEntity;
import ru.metaclone.service_auth.model.enums.Role;
import ru.metaclone.service_auth.model.service.TokenData;
import ru.metaclone.service_auth.repository.TokensRepository;

@Service
public class TokensService {

    private final TokenDataFactory tokenDataFactory;
    private final JwtService jwtService;
    private final TokensRepository tokensRepository;

    public TokensService(TokenDataFactory tokenDataFactory, JwtService jwtService, TokensRepository tokensRepository) {
        this.tokenDataFactory = tokenDataFactory;
        this.jwtService = jwtService;
        this.tokensRepository = tokensRepository;
    }


    public TokensResponse generateAndSaveTokens(Long credentialsId) {
        TokenData accessTokenData = tokenDataFactory.createAccessToken(credentialsId, Role.AUTHORISED_USER);
        TokenData refreshTokenData = tokenDataFactory.createAccessToken(credentialsId, Role.AUTHORISED_USER);
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
}
