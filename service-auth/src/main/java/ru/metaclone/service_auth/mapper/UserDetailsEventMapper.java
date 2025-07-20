package ru.metaclone.service_auth.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.service_auth.model.dto.UserDetailsEvent;
import ru.metaclone.service_auth.model.dto.UserDetails;

@Component
public class UserDetailsEventMapper {
    public UserDetailsEvent mapUserDetailsEvent(String userLogin, UserDetails userDetails) {

        return new UserDetailsEvent(userLogin, userDetails.firstName(), userDetails.lastName(), userDetails.birthday(), userDetails.gender());
    }
}
