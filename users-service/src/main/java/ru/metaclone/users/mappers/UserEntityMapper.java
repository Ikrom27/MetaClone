package ru.metaclone.users.mappers;

import org.springframework.stereotype.Component;
import ru.metaclone.users.models.dto.SaveUserDetailsRequest;
import ru.metaclone.users.models.entity.UserEntity;

@Component
public class UserEntityMapper {
    public UserEntity mapEntityFrom(SaveUserDetailsRequest saveUserDetailsRequest) {
        if (saveUserDetailsRequest == null) return null;

        return UserEntity.builder()
                .login(saveUserDetailsRequest.login())
                .firstName(saveUserDetailsRequest.firstName())
                .secondName(saveUserDetailsRequest.lastName())
                .gender(saveUserDetailsRequest.gender() != null ? saveUserDetailsRequest.gender().name() : null)
                .birthday(saveUserDetailsRequest.birthday())
                .build();
    }
}
