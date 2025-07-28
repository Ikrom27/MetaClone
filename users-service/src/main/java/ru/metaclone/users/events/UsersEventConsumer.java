package ru.metaclone.users.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.metaclone.users.data.events.UserAvatarUpdatedEvent;
import ru.metaclone.users.data.events.UserCreatedEvent;
import ru.metaclone.users.service.UsersService;

@Service
public class UsersEventConsumer {

    private final UsersService usersService;

    public UsersEventConsumer(UsersService usersService) {
        this.usersService = usersService;
    }

    @KafkaListener(
            topics = "${kafka.topic.user-created}",
            groupId = "${kafka.group-id.users-service}",
            containerFactory = "userCreatedContainerFactory"
    )
    public void consumeUserCreatedEvents(UserCreatedEvent userCreatedEvent) {
        usersService.createNewUserFromEvent(userCreatedEvent);
    }

    @KafkaListener(
            topics = "${kafka.topic.avatar-update}",
            groupId = "${kafka.group-id.users-service}",
            containerFactory = "avatarUpdatedContainerFactory"
    )
    public void consumeUserAvatarUpdatedEvent(UserAvatarUpdatedEvent userAvatarUpdatedEvent) {
        usersService.updateAvatarFromEvent(userAvatarUpdatedEvent);
    }
}
