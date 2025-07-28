package ru.metaclone.posts.exceptions;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends BaseException {
    public static final String CODE = "ACCESS_DENIED";

    public AccessDeniedException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
      return CODE;
    }

    @Override
    public HttpStatus getStatus() {
      return HttpStatus.FORBIDDEN;
    }
}
