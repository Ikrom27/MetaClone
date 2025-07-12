package ru.metaclone.service_auth.model.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentials {
    @NotBlank
    @Size(min = 4, max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "login incorrect symbols")
    private String login;

    @NotBlank
    @Size(min = 6, max = 100)
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+=-]+$", message = "password incorrect symbols")
    private String password;
}
