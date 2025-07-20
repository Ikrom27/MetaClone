package ru.metaclone.users.integration_tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.metaclone.users.exceptions.UserNotFoundException;
import ru.metaclone.users.repository.UsersRepository;
import ru.metaclone.users.utils.UserFactory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsersIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Test
    void getUserById_ExistingUser_ReturnsCorrectData() throws Exception {
        var saved = usersRepository.save(UserFactory.mockExistingUserEntity());
        var expectedResponse = UserFactory.loadFileAsString("response_mocks/get-user-by-id-OK.json");

        mockMvc.perform(get("/users/" + saved.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void getUserById_UserNotExist_ReturnsNotFoundError() throws Exception {
        mockMvc.perform(get("/users/" + 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errorCode").value(UserNotFoundException.CODE));
    }
}
