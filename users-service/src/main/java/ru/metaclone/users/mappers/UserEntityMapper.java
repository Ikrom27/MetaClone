package ru.metaclone.users.mappers;

import org.springframework.stereotype.Component;
import ru.metaclone.users.models.dto.SaveUserDetailsRequest;
import ru.metaclone.users.models.entity.UserEntity;
import ru.metaclone.users.models.events.UserCreatedEvent;

@Component
public class UserEntityMapper {
    public UserEntity mapEntityFrom(SaveUserDetailsRequest saveUserDetailsRequest) {
        return UserEntity.builder()
                .login(saveUserDetailsRequest.login())
                .firstName(saveUserDetailsRequest.firstName())
                .secondName(saveUserDetailsRequest.lastName())
                .gender(saveUserDetailsRequest.gender() != null ? saveUserDetailsRequest.gender().name() : null)
                .birthday(saveUserDetailsRequest.birthday())
                .build();
    }

    public UserEntity mapEntityFrom(UserCreatedEvent userCreatedEvent) {
        return UserEntity.builder()
                .login(userCreatedEvent.login())
                .firstName(userCreatedEvent.firstName())
                .secondName(userCreatedEvent.lastName())
                .gender(userCreatedEvent.gender() != null ? userCreatedEvent.gender().name() : null)
                .birthday(userCreatedEvent.birthday())
                .build();
    }
}
