package ru.metaclone.service_auth.exception;

import org.springframework.http.HttpStatus;

abstract class BaseException extends RuntimeException
{
    public BaseException(String message) {
        super(message);
    }

    abstract public String getCode();
    abstract public HttpStatus getStatus();
}
