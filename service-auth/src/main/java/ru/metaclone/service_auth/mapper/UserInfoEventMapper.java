package ru.metaclone.service_auth.mapper;

import org.springframework.stereotype.Component;
import ru.metaclone.service_auth.model.dto.UserInfoEvent;
import ru.metaclone.service_auth.model.service.UserInfo;

@Component
public class UserInfoEventMapper {
    public UserInfoEvent mapUserInfo(UserInfo userInfo) {
        return new UserInfoEvent(userInfo.firstName(), userInfo.lastName(), userInfo.birthday(), userInfo.gender());
    }
}
