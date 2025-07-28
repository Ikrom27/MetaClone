package ru.metaclone.posts.exceptions;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends BaseException {

    public static final String CODE = "POST_NOT_FOUND";

    public PostNotFoundException(String message) {
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
