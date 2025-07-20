package ru.metaclone.users.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;

@Configuration
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Bean
    public DefaultErrorHandler errorHandler() {
        return new DefaultErrorHandler((ConsumerRecord<?, ?> record, Exception ex) -> {
            log.warn("Deserialization error skipped: topic={}, partition={}, offset={}, error={}",
                    record.topic(), record.partition(), record.offset(), ex.getMessage());
        });
    }
}
