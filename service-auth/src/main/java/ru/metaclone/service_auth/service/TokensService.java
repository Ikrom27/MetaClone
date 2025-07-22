package ru.metaclone.service_auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.metaclone.service_auth.exception.InvalidTokenException;
import ru.metaclone.service_auth.exception.TokenNotFoundException;
import ru.metaclone.service_auth.exception.UserNotFountException;
import ru.metaclone.service_auth.mapper.TokenEntityMapper;
import ru.metaclone.service_auth.model.dto.TokensResponse;
import ru.metaclone.service_auth.model.service.TokenData;
import ru.metaclone.service_auth.repository.TokensRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TokensService {

    private final TokenDataFactory tokenDataFactory;
    private final JwtService jwtService;
    private final TokensRepository tokensRepository;
    private final TokenEntityMapper tokenEntityMapper;

    private static final String TOKEN_NOT_FOUND_MESSAGE = "Token not found, try login";


    public TokensResponse generateAndSaveTokens(Long userId, List<String> defaultAuthorities) {
        TokenData refreshTokenData = tokenDataFactory.createRefreshToken(defaultAuthorities, userId);
        TokenData accessTokenData = tokenDataFactory.createAccessToken(refreshTokenData);

        String accessJwt = jwtService.generateAccessToken(accessTokenData);
        String refreshJwt = jwtService.generateRefreshToken(refreshTokenData);
        tokensRepository.save(
                tokenEntityMapper.mapFromTokenData(refreshTokenData, refreshJwt)
        );
        return new TokensResponse(accessJwt, refreshJwt);
    }

    public TokensResponse refreshAccessToken(String refreshToken)
            throws InvalidTokenException, TokenNotFoundException, UserNotFountException {
        var refreshTokenData = jwtService.getRefreshTokenDataOrThrow(refreshToken);

        if (tokensRepository.findById(refreshTokenData.tokenId()).isEmpty()) {
            throw new TokenNotFoundException(TOKEN_NOT_FOUND_MESSAGE);
        }

        String accessToken = jwtService.generateAccessToken(
                tokenDataFactory.createAccessToken(refreshTokenData)
        );

        return new TokensResponse(accessToken, refreshToken);
    }
}
