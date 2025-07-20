package ru.metaclone.users.service;

import org.springframework.stereotype.Service;
import ru.metaclone.users.mappers.UserEntityMapper;
import ru.metaclone.users.models.dto.SaveUserDetailsRequest;
import ru.metaclone.users.models.dto.SaveUserResponse;
import ru.metaclone.users.models.entity.UserEntity;
import ru.metaclone.users.models.events.UserCreatedEvent;
import ru.metaclone.users.repository.UsersRepository;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final UserEntityMapper userEntityMapper;

    public UsersService(UsersRepository usersRepository, UserEntityMapper userEntityMapper) {
        this.usersRepository = usersRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public SaveUserResponse saveUserRequest(SaveUserDetailsRequest newUser) {
        var userEntity = userEntityMapper.mapEntityFrom(newUser);
        saveUserEntity(userEntity);
        return new SaveUserResponse(userEntity.getUserId(), userEntity.getLogin());
    }

    public void saveUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        var userEntity = userEntityMapper.mapEntityFrom(userCreatedEvent);
        saveUserEntity(userEntity);
    }

    private void saveUserEntity(UserEntity userEntity) {
        usersRepository.save(userEntity);
    }
}
