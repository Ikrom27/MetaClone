package ru.metaclone.service_auth.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InvalidTokenException extends BaseException {

    public static final String CODE = "INVALID_TOKEN";


    private String message;

    public InvalidTokenException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getCode() {
        return CODE;
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
