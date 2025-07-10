package ru.metaclone.service_auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.metaclone.service_auth.model.dto.UserInfoEvent;

@Component
public class UsersProducer {

    @Value("${secret.event-auth-topic}")
    private String AUTH_TOPIC;

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafka;

    public UsersProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafka = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendUserInfo(UserInfoEvent userInfoEvent) {
        try {
            String json = objectMapper.writeValueAsString(userInfoEvent);
            kafka.send(AUTH_TOPIC, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize UserInfoEvent for Kafka", e);
        }
    }
}
