package ru.metaclone.media.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidObjectKeyException extends BaseException {
    public final static String CODE = "INVALID_OBJECT_KEY";
    public InvalidObjectKeyException(String message) {
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
