package ru.metaclone.service_auth.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.metaclone.service_auth.configs.BasePostgresAndKafkaConfig;
import ru.metaclone.service_auth.exception.UserAlreadyExistException;
import ru.metaclone.service_auth.utils.TestDataFactory;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
public class RegisterIntegrationTest extends BasePostgresAndKafkaConfig {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void registerUser_givenValidRequest_shouldReturnTokens() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.registerRequest()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken", not(is(emptyString()))))
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.refreshToken", not(is(emptyString()))));
    }

    @Test
    public void registerUser_givenExistingUser_shouldReturnUserAlreadyExistsError() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.registerRequest()))
                .andExpect(status().isOk());

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.registerRequest()))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(UserAlreadyExistException.CODE))
                .andExpect(jsonPath("$.message", not(is(emptyString()))));
    }

    @Test
    public void registerUser_givenValidRequest_shouldSendUserDetailsMessageToKafka() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.registerRequest()))
                .andExpect(status().isOk());

        assertEquals(
                mapper.readTree(TestDataFactory.USER_DETAILS),
                mapper.readTree(consumeLastMessage())
        );
    }
}
