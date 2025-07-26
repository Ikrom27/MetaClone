package ru.metaclone.auth.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.auth.data.events.UserCreatedEvent;
import ru.metaclone.auth.data.requests.UserDetails;

@Component
public class UserDetailsEventMapper {
    public UserCreatedEvent mapUserDetailsEvent(Long userId, String userLogin, UserDetails userDetails) {

        return new UserCreatedEvent(userId, userLogin, userDetails.firstName(),
                userDetails.lastName(), userDetails.birthday(), userDetails.gender());
    }
}
