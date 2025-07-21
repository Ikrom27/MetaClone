package ru.metaclone.users.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends BaseException {

    public static final String CODE = "INVALID_TOKEN";

    public InvalidTokenException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
