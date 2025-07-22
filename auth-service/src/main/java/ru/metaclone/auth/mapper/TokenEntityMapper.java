package ru.metaclone.auth.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.auth.model.entity.TokenEntity;
import ru.metaclone.auth.model.service.TokenData;

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
