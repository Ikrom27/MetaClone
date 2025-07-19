package ru.metaclone.service_auth.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.metaclone.service_auth.exception.InvalidTokenException;
import ru.metaclone.service_auth.exception.UserAlreadyExistException;
import ru.metaclone.service_auth.exception.UserNotFountException;

import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
public class AuthIntegrationTest extends BasePostgresAndKafkaTest {

    @Autowired
    private MockMvc mvc;


    private final ObjectMapper mapper = new ObjectMapper();


    private static final String CREDENTIALS = """
    {
      "login": "testinglogin",
      "password": "testingpassword"
    }
    """;

    private static final String USER_DETAILS = """
    {
      "firstName": "User6",
      "lastName": "Doe",
      "birthday": "1996-03-23",
      "gender": "MALE"
    }
    """;

    private static final String REGISTER_REQUEST = """
    {
      "credentials": %s,
      "userDetails": %s
    }
    """.formatted(CREDENTIALS, USER_DETAILS);
    @Test
    public void registerUser_givenValidRequest_shouldReturnTokens() throws Exception {
        mvc.perform(post("/auth/register", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken", not(is(emptyString()))))
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.refreshToken", not(is(emptyString()))));
    }

    @Test
    void registerUser_givenExistingUser_shouldReturnUserAlreadyExistsError() throws Exception {
        // register first user
        mvc.perform(post("/auth/register", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        //register same user
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_REQUEST))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(UserAlreadyExistException.CODE))
                .andExpect(jsonPath("$.message", not(is(emptyString()))));
    }

    @Test
    public void registerUser_givenValidRequest_shouldSendUserDetailsMessageToKafka() throws Exception {
        mvc.perform(post("/auth/register", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        assertEquals(
                mapper.readTree(USER_DETAILS),
                mapper.readTree(consumeLastMessage())
        );
    }

    @Test
    public void refreshToken_givenValidRequest_shouldReturnNewAccessToken() throws Exception {
        var registerResult = mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_REQUEST))
                .andExpect(status().isOk())
                .andReturn();
        String json = registerResult.getResponse().getContentAsString();
        String refreshToken = mapper.readTree(json).get("refreshToken").asText();

        String refreshRequest = """
        {
          "refreshToken": "%s"
        }
        """.formatted(refreshToken);

        mvc.perform(post("/auth/refresh_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken", not(is(emptyString()))));
    }

    @Test
    public void refreshToken_givenInvalidToken_shouldReturnUnauthorized() throws Exception {
        String request = """
        {
          "refreshToken": "Bearer invalid.token.here"
        }
        """;

        mvc.perform(post("/auth/refresh_token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(InvalidTokenException.CODE))
                .andExpect(jsonPath("$.message", not(is(emptyString()))));
    }

    @Test
    public void login_givenValidCredentials_shouldReturnTokens() throws Exception {
        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_REQUEST))
                .andExpect(status().isOk());

        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CREDENTIALS))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken", not(is(emptyString()))))
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.refreshToken", not(is(emptyString()))));
    }

    @Test
    public void login_givenUnknownUser_shouldReturnUserNotFoundError() throws Exception {
        String unknownUserCredentials = """
        {
          "login": "nonexistentuser",
          "password": "somepassword"
        }
        """;

        mvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(unknownUserCredentials))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorCode").value(UserNotFountException.CODE))
                .andExpect(jsonPath("$.message", not(is(emptyString()))));
    }
}
