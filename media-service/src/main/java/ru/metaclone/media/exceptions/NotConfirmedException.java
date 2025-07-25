package ru.metaclone.media.exceptions;

import org.springframework.http.HttpStatus;

public class NotConfirmedException extends BaseException {
    public final static String CODE = "NOT_CONFIRMED";

    public NotConfirmedException(String message) {
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
