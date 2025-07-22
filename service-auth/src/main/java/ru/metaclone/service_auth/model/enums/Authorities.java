package ru.metaclone.service_auth.model.enums;

import lombok.Getter;

@Getter
public enum Authorities {

    ROLE_REFRESH_TOKEN("ROLE_REFRESH_TOKEN"),
    ROLE_USER("ROLE_USER");

    private final String value;

    Authorities(String value) {
        this.value = value;
    }
}
