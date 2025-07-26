package ru.metaclone.media.events;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.metaclone.media.data.events.UserAvatarUpdatedEvent;

@Service
public class KafkaProducer {

    @Value("${kafka.topic.avatar-update}")
    private String AVATAR_UPDATE_TOPIC;

    private final KafkaTemplate<String, UserAvatarUpdatedEvent> kafka;

    public KafkaProducer(KafkaTemplate<String, UserAvatarUpdatedEvent> kafka) {
        this.kafka = kafka;
    }

    public void produceAvatarUpdate(Long userId, String url) {
        try {
            kafka.send(AVATAR_UPDATE_TOPIC, new UserAvatarUpdatedEvent(userId, url));
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message ", e);
        }
    }
}