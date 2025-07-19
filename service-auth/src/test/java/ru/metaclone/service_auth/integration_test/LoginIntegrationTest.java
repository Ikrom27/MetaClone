package ru.metaclone.service_auth.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.metaclone.service_auth.configs.BasePostgresAndKafkaConfig;
import ru.metaclone.service_auth.exception.UserNotFountException;
import ru.metaclone.service_auth.utils.TestDataFactory;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
public class LoginIntegrationTest extends BasePostgresAndKafkaConfig {

    @Autowired
    private MockMvc mvc;

    @Test
    public void login_givenValidCredentials_shouldReturnTokens() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.registerRequest()))
                .andExpect(status().isOk());

        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.CREDENTIALS))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken", not(emptyString())))
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.refreshToken", not(emptyString())));
    }

    @Test
    public void login_givenUnknownUser_shouldReturnUserNotFoundError() throws Exception {
        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestDataFactory.UNKNOWN_USER_CREDENTIALS))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(UserNotFountException.CODE))
                .andExpect(jsonPath("$.message", not(emptyString())));
    }
}
