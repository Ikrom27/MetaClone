package ru.metaclone.auth.integration_test;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:29092",
                "port=29092"
        }
)
@Transactional
@Rollback
@ActiveProfiles("test")
public abstract class BaseTestingSetup {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Value("${kafka.topic.user-created}")
    private String topicName;

    protected Consumer<String, String> createConsumer() {
        Map<String, Object> props = createDefaultProps();
        props.put("key.deserializer", StringDeserializer.class);
        props.put("value.deserializer", StringDeserializer.class);
        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<String, String>(props).createConsumer();
        consumer.subscribe(Collections.singleton(topicName));
        return consumer;
    }

    protected String consumeLastMessage() {
        try (Consumer<String, String> consumer = createConsumer()) {
            ConsumerRecord<String, String> record = KafkaTestUtils.getRecords(consumer)
                    .records(topicName)
                    .iterator()
                    .next();
            return record.value();
        }
    }

    protected Map<String, Object> createDefaultProps() {
        return KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
    }
}
