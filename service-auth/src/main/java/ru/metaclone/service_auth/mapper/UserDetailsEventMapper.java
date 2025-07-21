package ru.metaclone.service_auth.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.service_auth.model.dto.UserDetailsEvent;
import ru.metaclone.service_auth.model.dto.UserDetails;

@Component
public class UserDetailsEventMapper {
    public UserDetailsEvent mapUserDetailsEvent(Long userId, String userLogin, UserDetails userDetails) {

        return new UserDetailsEvent(userId, userLogin, userDetails.firstName(),
                userDetails.lastName(), userDetails.birthday(), userDetails.gender());
    }
}
