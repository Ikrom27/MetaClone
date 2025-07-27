package ru.metaclone.media.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Authorities {
    ANONYMOUS("ROLE_ANONYMOUS"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;
}
