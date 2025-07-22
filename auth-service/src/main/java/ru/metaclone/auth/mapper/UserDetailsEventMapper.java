package ru.metaclone.auth.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.auth.model.dto.UserDetailsEvent;
import ru.metaclone.auth.model.dto.UserDetails;

@Component
public class UserDetailsEventMapper {
    public UserDetailsEvent mapUserDetailsEvent(Long userId, String userLogin, UserDetails userDetails) {

        return new UserDetailsEvent(userId, userLogin, userDetails.firstName(),
                userDetails.lastName(), userDetails.birthday(), userDetails.gender());
    }
}
