package ru.metaclone.users.integration_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.metaclone.users.integration_tests.kafka.KafkaProducer;
import ru.metaclone.users.repository.UsersRepository;
import ru.metaclone.users.utils.UserFactory;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@ActiveProfiles("test")
public class EventsTest extends BaseIntegrationTest {
    @Autowired
    private KafkaProducer producer;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MockMvc mockMvc;

    @Value("${kafka.topic.user-created}")
    protected String USER_CREATED_EVENT;

    @Value("${kafka.topic.user-avatar-update}")
    protected String USER_AVATAR_UPDATE_TOPIC;

    @BeforeEach
    void cleanUp() {
        usersRepository.deleteAll();
    }

    @Test
    public void saveUserFromKafka_userSavedOnDataBase() {
        var userCreatedEvent = UserFactory.mockUserCreatedEvent();
        producer.send(USER_CREATED_EVENT, userCreatedEvent);

        var expectedResponse = UserFactory.loadFileAsString("response_mocks/get-kafka-saved-user-by-id-OK.json");

        await().atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> mockMvc.perform(get("/users/v1/" + userCreatedEvent.userId()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(expectedResponse))
                );
    }

    @Test
    public void updateUserAvatar_givenExistingUser_shouldUpdateAvatar() {
        var user = UserFactory.mockExistingUserEntity();
        usersRepository.save(user);

        var userAvatarUpdatedEvent = UserFactory.mockUserAvatarUpdatedEvent();
        producer.send(USER_AVATAR_UPDATE_TOPIC, userAvatarUpdatedEvent);

        await().atMost(5, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    var updatedUser = usersRepository.findById(user.getUserId()).orElseThrow();
                    assertThat(updatedUser.getAvatarUrl()).isEqualTo(userAvatarUpdatedEvent.avatarUrl());
                });
    }
}
