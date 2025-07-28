package ru.metaclone.search.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.search.data.documents.UserPreviewDocument;
import ru.metaclone.search.data.events.UserCreatedEvent;
import ru.metaclone.search.data.events.UserUpdatedEvent;

@Component
public class UserPreviewDocumentMapper {
    public UserPreviewDocument mapFrom(UserCreatedEvent userCreatedEvent) {
        return UserPreviewDocument.builder()
                .userId(userCreatedEvent.userId())
                .login(userCreatedEvent.login())
                .firstName(userCreatedEvent.firstName())
                .lastName(userCreatedEvent.lastName())
                .build();
    }

    public UserPreviewDocument mapFrom(UserUpdatedEvent userUpdatedEvent) {
        return UserPreviewDocument.builder()
                .userId(userUpdatedEvent.userId())
                .login(userUpdatedEvent.login())
                .firstName(userUpdatedEvent.firstName())
                .lastName(userUpdatedEvent.lastName())
                .avatarUrl(userUpdatedEvent.avatarUrl())
                .build();
    }
}
