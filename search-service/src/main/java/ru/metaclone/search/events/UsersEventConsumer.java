package ru.metaclone.search.events;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.metaclone.search.data.events.UserUpdatedEvent;
import ru.metaclone.search.data.events.UserCreatedEvent;
import ru.metaclone.search.mapper.UserPreviewDocumentMapper;
import ru.metaclone.search.service.SearchService;

@Service
@AllArgsConstructor
public class UsersEventConsumer {

    private final SearchService searchService;
    private final UserPreviewDocumentMapper userPreviewDocumentMapper;

    @KafkaListener(
            topics = "${kafka.topic.users-created}",
            containerFactory = "userCreatedContainerFactory"
    )
    public void consumeUserCreatedEvents(UserCreatedEvent userCreatedEvent) {
        searchService.saveUserPreview(userPreviewDocumentMapper.mapFrom(userCreatedEvent));
    }

    @KafkaListener(
            topics = "${kafka.topic.users-updated}",
            containerFactory = "userUpdatedContainerFactory"
    )
    public void consumeUserUpdatedEvents(UserUpdatedEvent userUpdatedEvent) {
        searchService.saveUserPreview(userPreviewDocumentMapper.mapFrom(userUpdatedEvent));
    }
}
