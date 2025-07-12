package ru.metaclone.service_auth.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.service_auth.model.dto.UserDetailsEvent;
import ru.metaclone.service_auth.model.service.UserDetails;

@Component
public class UserDetailsEventMapper {
    public UserDetailsEvent mapUserDetailsEvent(UserDetails userDetails) {
        return new UserDetailsEvent(userDetails.firstName(), userDetails.lastName(), userDetails.birthday(), userDetails.gender());
    }
}
