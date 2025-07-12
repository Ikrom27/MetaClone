package ru.metaclone.service_auth.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.metaclone.service_auth.exception.UserNotFountException;
import ru.metaclone.service_auth.mapper.UserDetailsEventMapper;
import ru.metaclone.service_auth.model.dto.UserDetailsEvent;
import ru.metaclone.service_auth.model.enums.Gender;
import ru.metaclone.service_auth.model.dto.UserCredentials;
import ru.metaclone.service_auth.model.dto.TokensResponse;
import ru.metaclone.service_auth.model.dto.UserDetails;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import ru.metaclone.service_auth.exception.UserAlreadyExistException;

import java.time.LocalDate;

class AuthServiceTest {

    @Mock
    TokensService tokensService;

    @Mock
    CredentialsService credentialsService;

    @Mock
    UsersDetailsProducer usersDetailsProducer;

    @Mock
    UserDetailsEventMapper userDetailsEventMapper;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginUser_Success() throws UserNotFountException {
        var credentials = new UserCredentials("login", "password");
        Long userId = 123L;
        var expectedTokens = new TokensResponse("accessToken", "refreshToken");

        when(credentialsService.getUserIdByLogin("login")).thenReturn(userId);
        when(tokensService.generateAndSaveTokens(userId)).thenReturn(expectedTokens);

        var tokensResponse = authService.loginUser(credentials);

        assertNotNull(tokensResponse);
        assertEquals("accessToken", tokensResponse.accessToken());
        assertEquals("refreshToken", tokensResponse.refreshToken());

        verify(credentialsService).getUserIdByLogin("login");
        verify(tokensService).generateAndSaveTokens(userId);
    }

    @Test
    void loginUser_UserNotFound_Throws() {
        var credentials = new UserCredentials("login", "password");
        when(credentialsService.getUserIdByLogin("login")).thenReturn(null);

        assertThrows(UserNotFountException.class, () -> authService.loginUser(credentials));
        verify(credentialsService).getUserIdByLogin("login");
        verifyNoInteractions(tokensService);
    }

    @Test
    void registerUser_Success() throws UserAlreadyExistException {
        var credentials = new UserCredentials("login", "password");
        var userDetails = new UserDetails("name", "lastName",
                LocalDate.of(1995, 7, 12), Gender.MALE);
        Long userId = 123L;
        var expectedTokens = new TokensResponse("accessToken", "refreshToken");
        var userDetailsEvent = new UserDetailsEvent("name", "lastName",
                LocalDate.of(1995, 7, 12), Gender.MALE);

        when(credentialsService.isUserExistWithLogin("login")).thenReturn(false);
        when(credentialsService.saveUserCredential(credentials)).thenReturn(userId);
        when(userDetailsEventMapper.mapUserDetailsEvent(userDetails)).thenReturn(userDetailsEvent);
        when(tokensService.generateAndSaveTokens(userId)).thenReturn(expectedTokens);

        var tokensResponse = authService.registerUser(credentials, userDetails);

        assertNotNull(tokensResponse);
        assertEquals("accessToken", tokensResponse.accessToken());
        assertEquals("refreshToken", tokensResponse.refreshToken());

        verify(credentialsService).isUserExistWithLogin("login");
        verify(credentialsService).saveUserCredential(credentials);
        verify(userDetailsEventMapper).mapUserDetailsEvent(userDetails);
        verify(usersDetailsProducer).sendUserInfo(userDetailsEvent);
        verify(tokensService).generateAndSaveTokens(userId);
    }

    @Test
    void registerUser_UserAlreadyExists_Throws() {
        var credentials = new UserCredentials("login", "password");
        var userDetails = new UserDetails("name", "lastName",
                LocalDate.of(1995, 7, 12), Gender.MALE);

        when(credentialsService.isUserExistWithLogin("login")).thenReturn(true);

        assertThrows(UserAlreadyExistException.class, () -> authService.registerUser(credentials, userDetails));

        verify(credentialsService).isUserExistWithLogin("login");
        verifyNoMoreInteractions(credentialsService, tokensService, usersDetailsProducer, userDetailsEventMapper);
    }

    @Test
    void refreshAccessToken_DelegatesToTokensService() {
        String refreshToken = "someRefreshToken";
        var expectedTokens = new TokensResponse("accessToken", "refreshToken");

        when(tokensService.refreshAccessToken(refreshToken)).thenReturn(expectedTokens);

        var result = authService.refreshAccessToken(refreshToken);

        assertEquals(expectedTokens, result);
        verify(tokensService).refreshAccessToken(refreshToken);
        verifyNoInteractions(credentialsService, usersDetailsProducer, userDetailsEventMapper);
    }
}
