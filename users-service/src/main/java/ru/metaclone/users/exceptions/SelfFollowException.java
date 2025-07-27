package ru.metaclone.users.exceptions;

import org.springframework.http.HttpStatus;

public class SelfFollowException extends BaseException {

    public static final String CODE = "SELF_FOLLOW_ERROR";

    public SelfFollowException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
