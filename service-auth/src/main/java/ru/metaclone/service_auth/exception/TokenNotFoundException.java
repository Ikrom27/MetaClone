package ru.metaclone.service_auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class TokenNotFoundException extends BaseException {

    public static final String CODE = "TOKEN_NOT_FOUND";


    private String message;

    public TokenNotFoundException(String message) {
        super(message);
        this.message = message;
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
