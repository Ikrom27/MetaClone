package ru.metaclone.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.metaclone.auth.exception.InvalidTokenException;
import ru.metaclone.auth.model.service.TokenData;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${secret.access-token-key}")
    private String ACCESS_TOKEN_KEY;

    @Value("${secret.refresh-token-key}")
    private String REFRESH_TOKEN_KEN;

    @Value("${secret.access-token-ttl}")
    private Long ACCESS_TOKEN_TTL;

    @Value("${secret.refresh-token-ttl}")
    private Long REFRESH_TOKEN_TTL;

    private static final String AUTHORITIES_KEY = "AUTHORITIES";
    private static final String USER_ID_KEY = "USER_ID";

    public String generateAccessToken(TokenData tokenData) {
        return generateToken(tokenData, ACCESS_TOKEN_KEY);
    }

    public String generateRefreshToken(TokenData tokenData) {
        return generateToken(tokenData, REFRESH_TOKEN_KEN);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token, ACCESS_TOKEN_KEY);
            return true;
        } catch (InvalidTokenException ex) {
            return false;
        }
    }

    public TokenData getAccessTokenDataOrThrow(String accessToken) throws InvalidTokenException {
        return getTokenDataOrThrow(accessToken, ACCESS_TOKEN_KEY);
    }

    public TokenData getRefreshTokenDataOrThrow(String refreshToken) throws InvalidTokenException {
        return getTokenDataOrThrow(refreshToken, REFRESH_TOKEN_KEN);
    }


    private TokenData getTokenDataOrThrow(String token, String key) throws InvalidTokenException {
        var claims = parseToken(token, key);

        Long userId = claims.get(USER_ID_KEY, Number.class).longValue();
        List<String> authorities = claims.get(AUTHORITIES_KEY, List.class);
        Long issuedAt = claims.getIssuedAt().getTime();
        Long expiresAt = claims.getExpiration().getTime();
        UUID tokenId = UUID.fromString(claims.getSubject());

        return new TokenData(tokenId, userId, authorities, issuedAt, expiresAt);
    }

    private String generateToken(TokenData tokenData, String key) {
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(tokenData.tokenId().toString())
                .claim(USER_ID_KEY, tokenData.userId())
                .claim(AUTHORITIES_KEY, tokenData.authorities())
                .issuedAt(new Date(tokenData.issuedAt()))
                .expiration(new Date(tokenData.expiresAt()))
                .signWith(secretKey)
                .compact();
    }

    private Claims parseToken(String token, String key) throws InvalidTokenException {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }
}
