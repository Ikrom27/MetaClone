package ru.metaclone.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.metaclone.auth.data.events.UserCreatedEvent;

@Component
public class UsersDetailsProducer {

        @Value("${kafka.topic.user-created}")
        private String AUTH_TOPIC;

    private final KafkaTemplate<String, UserCreatedEvent> kafka;

    public UsersDetailsProducer(KafkaTemplate<String, UserCreatedEvent> kafkaTemplate) {
        this.kafka = kafkaTemplate;
    }

    public void sendUserInfo(UserCreatedEvent userCreatedEvent) {
        try {
            kafka.send(AUTH_TOPIC, userCreatedEvent);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message for Kafka ", e);
        }
    }
}
