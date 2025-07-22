package ru.metaclone.service_auth.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.metaclone.service_auth.model.enums.Authorities;
import ru.metaclone.service_auth.model.service.TokenData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class TokenDataFactory {

    private static final String GRANT_PREFIX = "GRANT_";

    @Value("${secret.access-token-ttl}")
    private Long accessTokenTTL;

    @Value("${secret.refresh-token-ttl}")
    private Long refreshTokenTTL;

    public TokenData createAccessToken(TokenData refreshToken) {
        var authorities = refreshToken.authorities().stream()
                .filter(authority -> authority.startsWith(GRANT_PREFIX))
                .map(authority -> authority.substring(GRANT_PREFIX.length()))
                .toList();
        long now = System.currentTimeMillis();
        return new TokenData(refreshToken.tokenId(), refreshToken.userId(), authorities, now, now + refreshTokenTTL);
    }

    public TokenData createRefreshToken(List<String> defaultAuthorities, Long userId) {
        var authorities = new ArrayList<String>();
        authorities.add(Authorities.ROLE_REFRESH_TOKEN.getValue());
        authorities.add(Authorities.ROLE_USER.getValue());
        defaultAuthorities
                .stream()
                .map(authority -> GRANT_PREFIX + authority)
                .forEach(authorities::add);

        var now = System.currentTimeMillis();
        return new TokenData(UUID.randomUUID(), userId, authorities, now, now + refreshTokenTTL);
    }
}