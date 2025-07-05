package ru.metaclone.service_auth.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvalidTokenException extends RuntimeException {
    private String message;

    public InvalidTokenException(String message) {
        super(message);
        this.message = message;
    }
}
