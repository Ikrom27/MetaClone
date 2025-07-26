package ru.metaclone.users.secury;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.metaclone.users.exceptions.InvalidTokenException;
import ru.metaclone.users.exceptions.TokenExpiredException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${secret.access-token-key}")
    private String ACCESS_TOKEN_KEY;

    private static final String AUTHORITIES_KEY = "AUTHORITIES";
    private static final String USER_ID_KEY = "USER_ID";

    public UsernamePasswordAuthenticationToken extractUserAuthenticationFromToken(String token) {
        var claims = parseToken(token, ACCESS_TOKEN_KEY);
        Long userId = claims.get(USER_ID_KEY, Number.class).longValue();
        List<String> authorities = claims.get(AUTHORITIES_KEY, List.class);
        var principal = new UserContext(userId, authorities);
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );
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
