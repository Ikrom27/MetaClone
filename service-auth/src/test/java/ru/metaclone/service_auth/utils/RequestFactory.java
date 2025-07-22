package ru.metaclone.service_auth.utils;

public class RequestFactory {

    public static String mockRegisterRequest(String credentials, String userDetails) {
        return """
        {
          "credentials": %s,
          "userDetails": %s
        }
        """.formatted(credentials, userDetails);
    }

    public static String mockRefreshTokenRequest(String token) {
        return """
                {
                  "refresh_token": "%s"
                }
                """.formatted(token);
    }
}
