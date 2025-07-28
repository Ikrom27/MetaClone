package ru.metaclone.posts.exceptions;

import org.springframework.http.HttpStatus;

abstract public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }

  abstract public String getCode();
  abstract public HttpStatus getStatus();
}
