package ru.metaclone.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.metaclone.auth.exception.InvalidTokenException;
import ru.metaclone.auth.exception.TokenNotFoundException;
import ru.metaclone.auth.exception.UserNotFountException;
import ru.metaclone.auth.mapper.TokenEntityMapper;
import ru.metaclone.auth.model.dto.TokensResponse;
import ru.metaclone.auth.model.enums.Authorities;
import ru.metaclone.auth.model.enums.Role;
import ru.metaclone.auth.model.service.TokenData;
import ru.metaclone.auth.repository.TokensRepository;

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

    public void logout(String refreshToken) {
        var refreshTokenData = jwtService.getRefreshTokenDataOrThrow(refreshToken);
        tokensRepository.deleteById(refreshTokenData.tokenId());
    }
}
