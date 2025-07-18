package ru.metaclone.service_auth.integration_test;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.Map;

@Testcontainers
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:29092",
                "port=29092"
        }
)
public class BaseConfigTest {
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Container
    protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:15.2")
                    .withDatabaseName("some_name")
                    .withUsername("some_user")
                    .withPassword("some_pass");

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);

        registry.add("secret.access-token-key", () -> "SDFSDFS34234DSFSDFDSDDFASDF32432J3H41L2JKH34KJL12H3J414H2L");
        registry.add("secret.refresh-token-key", () -> "SDFASDF4F3F34242FASDFASDF32432J3H41L2JKH34KJL12H3J414H2L");
        registry.add("secret.access-token-ttl", () -> "3600000");
        registry.add("secret.refresh-token-ttl", () -> "86400000");
        registry.add("secret.event-auth-topic", () -> "auth-topic");

        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:29092");
        registry.add("spring.kafka.consumer.group-id", () -> "testGroup");
    }

    protected Consumer<String, String> createConsumerByTopic(String topic) {
        Map<String, Object> props = KafkaTestUtils.consumerProps("groupId", "true", embeddedKafka);
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(props).createConsumer();
        consumer.subscribe(Collections.singleton(topic));
        return consumer;
    }

    protected String consumeSingleMessageFromTopic(String topic) {
        Consumer<String, String> consumer = createConsumerByTopic(topic);
        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, topic);

        String json = record.value();
        consumer.close();

        return json;
    }
}
