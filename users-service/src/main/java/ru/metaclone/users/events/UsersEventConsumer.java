package ru.metaclone.users.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.metaclone.users.models.events.UserCreatedEvent;
import ru.metaclone.users.service.UsersService;

@Service
public class UsersEventConsumer {

    private final UsersService usersService;

    public UsersEventConsumer(UsersService usersService) {
        this.usersService = usersService;
    }

    @KafkaListener(
            topics = "${kafka.topic.user-events}",
            groupId = "${kafka.group-id.users}"
    )
    public void consume(UserCreatedEvent userCreatedEvent) {
        usersService.saveUserCreatedEvent(userCreatedEvent);
    }
}
