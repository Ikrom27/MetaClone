package ru.metaclone.service_auth.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.metaclone.service_auth.model.enums.Role;
import ru.metaclone.service_auth.model.service.TokenData;

@Service
public class TokenDataFactory {

    @Value("${secret.access-token-ttl}")
    private Long accessTokenTTL;

    @Value("${secret.refresh-token-ttl}")
    private Long refreshTokenTTL;

    public TokenData createAccessToken(Long userId, Role role) {
        long now = System.currentTimeMillis();
        return new TokenData(userId, role, now, now + accessTokenTTL);
    }

    public TokenData createRefreshToken(Long userId, Role role) {
        long now = System.currentTimeMillis();
        return new TokenData(userId, role, now, now + refreshTokenTTL);
    }
}