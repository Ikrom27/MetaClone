package ru.metaclone.service_auth.integration_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

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
public class AuthTest extends BaseConfigTest {

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

    private static final String TEST_TOPIC = "auth-topic";


    @Test
    public void registerNewUser() throws Exception {
        mvc.perform(post("/auth/register", "")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REGISTER_REQUEST))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken", not(is(emptyString()))))
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.refreshToken", not(is(emptyString()))));;

        assertEquals(
                mapper.readTree(USER_DETAILS),
                mapper.readTree(consumeSingleMessageFromTopic(TEST_TOPIC))
        );
    }
}
