package ru.metaclone.auth.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.metaclone.auth.exception.InvalidTokenException;
import ru.metaclone.auth.repository.TokensRepository;
import ru.metaclone.auth.utils.DataMocks;
import ru.metaclone.auth.utils.RequestFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
public class TokenIntegrationTest extends BaseTestingSetup {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TokensRepository tokensRepository;

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

    @Test
    public void logout_givenValidUser_shouldRemoveRefreshToken() throws Exception {
        var registerRequest = RequestFactory.mockRegisterRequest(DataMocks.CREDENTIALS, DataMocks.USER_DETAILS);
        var registerResult = mvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isOk())
                .andReturn();

        String json = registerResult.getResponse().getContentAsString();
        String refreshToken = mapper.readTree(json).get("refreshToken").asText();

        assertThat(tokensRepository.count()).isEqualTo(1);

        var logoutRequest = RequestFactory.mockLogoutRequest(refreshToken);

        mvc.perform(post("/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(logoutRequest))
                .andExpect(status().isOk());

        assertThat(tokensRepository.count()).isEqualTo(0);
    }
}
