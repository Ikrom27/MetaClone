package ru.metaclone.users.config.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.metaclone.users.exceptions.InvalidTokenException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenProvider {

    @Value("${secret.access-token-key}")
    private String accessTokenKey;

    public Long getUserId(String token) {
        Claims claims = parseToken(token, accessTokenKey);
        return claims.get("userId", Long.class);
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token, accessTokenKey);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
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
