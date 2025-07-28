package ru.metaclone.users.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.metaclone.users.data.events.UserUpdatedEvent;

@Service
public class UsersEventProducer {

    private final Logger logger = LoggerFactory.getLogger(UsersEventProducer.class);

    @Value("${kafka.topic.user-updated}")
    private String USER_UPDATED_TOPIC;

    private final KafkaTemplate<String, UserUpdatedEvent> kafka;

    public UsersEventProducer(KafkaTemplate<String, UserUpdatedEvent> kafka) {
        this.kafka = kafka;
    }

    public void produceUserUpdateEvent(UserUpdatedEvent userUpdatedEvent) {
        try {
            kafka.send(USER_UPDATED_TOPIC, userUpdatedEvent);
        } catch (Exception e) {
            logger.error("Failed to send UserUpdatedEvent to topic {} for user_id={}, error={}",
                    USER_UPDATED_TOPIC,
                    userUpdatedEvent.userId(),
                    e.getMessage(),
                    e);
        }
    }
}
