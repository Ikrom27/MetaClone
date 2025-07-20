package ru.metaclone.users.integration_tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.metaclone.users.integration_tests.kafka.KafkaProducer;
import ru.metaclone.users.utils.UserFactory;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@ActiveProfiles("test")
public class KafkaConsumerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private KafkaProducer producer;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void saveUserFromKafka_userSavedOnDataBase() throws Exception {
        var userCreatedEvent =  UserFactory.mockUserCreatedEvent();
        producer.send(TEST_TOPIC_NAME, userCreatedEvent);

        var expectedResponse = UserFactory.loadFileAsString("response_mocks/get-kafka-saved-user-by-id-OK.json");

        await().atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> mockMvc.perform(get("/users/" + userCreatedEvent.userId()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse))
                );
    }
}
