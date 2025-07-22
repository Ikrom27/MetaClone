package ru.metaclone.service_auth.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.service_auth.model.entity.TokenEntity;
import ru.metaclone.service_auth.model.service.TokenData;

@Component
public class TokenEntityMapper {
    public TokenEntity mapFromTokenData(TokenData tokenData, String serializedToken) {
        return TokenEntity.builder()
                .tokenId(tokenData.tokenId())
                .userId(tokenData.userId())
                .refreshToken(serializedToken)
                .createdAt(tokenData.expiresAt())
                .expiresAt(tokenData.expiresAt())
                .build();
    }
}
