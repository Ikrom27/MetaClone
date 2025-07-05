package ru.metaclone.service_auth.model.enums;

public enum Role {
    ADMIN("admin"),
    AUTHORISED_USER("authorised_user");
    private final String value;

    Role(String value) {
        this.value = value;
    }
}
