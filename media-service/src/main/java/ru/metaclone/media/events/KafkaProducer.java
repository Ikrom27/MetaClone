package ru.metaclone.media.events;

import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    public void produceAvatarUpdate(Long userId, String url) {
        System.out.println("AVATAR UPDATED " + url);
    }
}
