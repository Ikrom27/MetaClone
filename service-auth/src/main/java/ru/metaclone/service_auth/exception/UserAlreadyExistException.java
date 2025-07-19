package ru.metaclone.service_auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UserAlreadyExistException extends BaseException{
    private String message;
    public static final String CODE = "USER_ALREADY_EXISTS";

    public UserAlreadyExistException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
