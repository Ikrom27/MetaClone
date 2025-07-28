package ru.metaclone.posts.security.exceptions;

import org.springframework.http.HttpStatus;

abstract public class BaseSecurityException extends RuntimeException {
    public BaseSecurityException(String message) {
        super(message);
    }

  abstract public String getCode();
  abstract public HttpStatus getStatus();
}
