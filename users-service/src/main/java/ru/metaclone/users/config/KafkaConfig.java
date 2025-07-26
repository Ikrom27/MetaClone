package ru.metaclone.users.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.metaclone.users.data.events.UserAvatarUpdatedEvent;
import ru.metaclone.users.data.events.UserCreatedEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.group-id.users-service}")
    private String groupId;

    private <T> ConsumerFactory<String, T> eventConsumerFactory(Class<T> eventClass) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.metaclone.users.data.events");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, eventClass.getName());
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                new JsonDeserializer<>(eventClass, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> userCreatedContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent>();
        factory.setConsumerFactory(eventConsumerFactory(UserCreatedEvent.class));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserAvatarUpdatedEvent> avatarUpdatedContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, UserAvatarUpdatedEvent>();
        factory.setConsumerFactory(eventConsumerFactory(UserAvatarUpdatedEvent.class));
        return factory;
    }
}
