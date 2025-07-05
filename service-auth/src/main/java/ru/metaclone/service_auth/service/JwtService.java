package ru.metaclone.service_auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.metaclone.service_auth.exception.InvalidTokenException;
import ru.metaclone.service_auth.model.service.TokenData;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${secret.access-token-key}")
    private String accessTokenKey;

    @Value("${secret.refresh-token-key}")
    private String refreshTokenKey;

    @Value("${secret.access-token-ttl}")
    private Long accessTokenTTL;

    @Value("${secret.refresh-token-ttl}")
    private Long refreshTokenTTL;

    public String generateAccessToken(TokenData tokenData) {
        return generateToken(tokenData, accessTokenKey);
    }

    public String generateRefreshToken(TokenData tokenData) {
        return generateToken(tokenData, refreshTokenKey);
    }


    public Long getRefreshTokenUserId(String token) throws InvalidTokenException {
        return Long.parseLong(parseToken(refreshTokenKey, token).getSubject());
    }

    private String generateToken(TokenData tokenData, String key) {
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(tokenData.userId().toString())
                .claim("role", tokenData.role().name())
                .issuedAt(new Date(tokenData.issuedAt()))
                .expiration(new Date(tokenData.expiredAt()))
                .signWith(secretKey)
                .compact();
    }

    private Claims parseToken(String key, String token) throws InvalidTokenException {
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
