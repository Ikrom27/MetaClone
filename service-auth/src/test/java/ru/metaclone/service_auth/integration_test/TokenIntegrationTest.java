package ru.metaclone.service_auth.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.metaclone.service_auth.configs.BasePostgresAndKafkaConfig;
import ru.metaclone.service_auth.exception.InvalidTokenException;
import ru.metaclone.service_auth.utils.DataMocks;
import ru.metaclone.service_auth.utils.RequestFactory;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
public class TokenIntegrationTest extends BasePostgresAndKafkaConfig {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void refreshToken_givenValidRequest_shouldReturnNewAccessToken() throws Exception {
        var registerRequest = RequestFactory.mockRegisterRequest(DataMocks.CREDENTIALS, DataMocks.USER_DETAILS);
        var registerResult = mvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isOk())
                .andReturn();

        String json = registerResult.getResponse().getContentAsString();
        String refreshToken = mapper.readTree(json).get("refreshToken").asText();

        String refreshRequest = RequestFactory.mockRefreshTokenRequest(refreshToken);

        mvc.perform(post("/v1/auth/refresh_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken", not(emptyString())));
    }

    @Test
    public void refreshToken_givenInvalidToken_shouldReturnUnauthorized() throws Exception {
        String request = RequestFactory.mockRefreshTokenRequest("invalid token");

        mvc.perform(post("/v1/auth/refresh_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(InvalidTokenException.CODE))
                .andExpect(jsonPath("$.message", not(emptyString())));
    }
}
