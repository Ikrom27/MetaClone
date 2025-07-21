package ru.metaclone.users.secury;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.metaclone.users.exceptions.InvalidTokenException;
import ru.metaclone.users.exceptions.TokenExpiredException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenProvider {

    @Value("${secret.access-token-key}")
    private String accessTokenKey;

    public Long getUserIdOrThrow(String token) {
        Claims claims = parseToken(token, accessTokenKey);
        return claims.get("userId", Long.class);
    }

    private Claims parseToken(String token, String key) throws InvalidTokenException {
        try {
            SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch (ExpiredJwtException ex) {
            throw new TokenExpiredException(ex.getMessage());
        } catch (Exception ex) {
            throw new InvalidTokenException(ex.getMessage());
        }
    }
}
