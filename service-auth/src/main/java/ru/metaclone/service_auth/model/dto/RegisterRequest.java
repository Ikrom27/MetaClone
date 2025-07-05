package ru.metaclone.service_auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.metaclone.service_auth.model.service.UserCredentials;
import ru.metaclone.service_auth.model.service.UserInfo;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private UserCredentials credentials;
    private UserInfo userInfo;
}
