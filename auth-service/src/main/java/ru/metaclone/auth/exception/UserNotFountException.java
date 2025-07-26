package ru.metaclone.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFountException extends BaseException {
    private String message;

    public static final String CODE = "USER_NOT_FOUND";

    public UserNotFountException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
