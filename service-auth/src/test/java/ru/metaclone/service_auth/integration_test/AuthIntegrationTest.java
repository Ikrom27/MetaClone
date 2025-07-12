package ru.metaclone.service_auth.integration_test;

import jakarta.transaction.Transactional;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
public class AuthIntegrationTest {
    @Container
    private static final KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1")
    );

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:15.3"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private static final String userDetailsJson =
            "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"birthday\":\"1990-05-20\",\"gender\":\"MALE\"}";

    private static final String registerJson = String.format("""
{
    "credentials": {
        "login": "testuser23",
        "password": "TestPass123!"
    },
    "userDetails": %s
}
""", userDetailsJson);


    @Autowired
    private WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("DB_HOST", () -> "localhost");
        registry.add("DB_NAME", () -> "metaclone_auth");
        registry.add("DB_PASSWORD", () -> "postgres");
        registry.add("DB_PORT", () -> "5432");
        registry.add("DB_USERNAME", () -> "postgres");

        registry.add("SECRET_ACCESS_TOKEN_KEY", () -> "SDFSDFS34234DSFSDFDSDDFASDF32432J3H41L2JKH34KJL12H3J414H2L");
        registry.add("SECRET_ACCESS_TOKEN_TTL", () -> "3600000");

        registry.add("SECRET_REFRESH_TOKEN_KEY", () -> "SDFASDF4F3F34242FASDFASDF32432J3H41L2JKH34KJL12H3J414H2L");
        registry.add("SECRET_REFRESH_TOKEN_TTL", () -> "86400000");

        registry.add("EVENT_AUTH_TOPIC", () -> "auth-topic");
    }


    private Consumer<String, String> consumer;

    @BeforeEach
    void setupConsumer() {
        Map<String, Object> props = new HashMap<>(KafkaTestUtils.consumerProps(kafkaContainer.getBootstrapServers(), "testGroup", "true"));
        consumer = new KafkaConsumer<>(props, new StringDeserializer(), new StringDeserializer());
        consumer.subscribe(List.of("auth-topic"));

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    public void registerUser() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    public void registerUserAndCheckKafkaMessage() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());

        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertTrue(records.count() > 0);

        ConsumerRecord<String, String> record = records.iterator().next();
        String message = record.value();
        assertTrue(message.contentEquals(userDetailsJson));
    }
}