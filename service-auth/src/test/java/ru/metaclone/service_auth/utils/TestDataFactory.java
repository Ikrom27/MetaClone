package ru.metaclone.service_auth.utils;

public class TestDataFactory {

    public static final String CREDENTIALS = """
        {
          "login": "testinglogin",
          "password": "testingpassword"
        }
        """;

    public static final String UNKNOWN_USER_CREDENTIALS = """
        {
          "login": "nonexistentuser",
          "password": "somepassword"
        }
        """;

    public static final String USER_DETAILS = """
        {
          "firstName": "User6",
          "lastName": "Doe",
          "birthday": "1996-03-23",
          "gender": "MALE"
        }
        """;

    public static final String USER_CREATED_EVENT = """
        {
          "login": "testinglogin",
          "firstName": "User6",
          "lastName": "Doe",
          "birthday": "1996-03-23",
          "gender": "MALE"
        }
        """;

    public static final String REGISTER_REQUEST = """
        {
          "credentials": %s,
          "userDetails": %s
        }
        """.formatted(CREDENTIALS, USER_DETAILS);

    public static String registerRequest() {
        return REGISTER_REQUEST;
    }
}
