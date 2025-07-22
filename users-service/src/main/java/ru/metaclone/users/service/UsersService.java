package ru.metaclone.users.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ru.metaclone.users.exceptions.UserNotFoundException;
import ru.metaclone.users.mappers.UserEntityMapper;
import ru.metaclone.users.models.dto.SaveUserDetailsRequest;
import ru.metaclone.users.models.dto.SaveUserResponse;
import ru.metaclone.users.models.dto.UserResponse;
import ru.metaclone.users.models.entity.UserEntity;
import ru.metaclone.users.models.events.UserAvatarUpdatedEvent;
import ru.metaclone.users.models.events.UserCreatedEvent;
import ru.metaclone.users.repository.UsersRepository;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    private final UserEntityMapper userEntityMapper;

    private static final String USER_NOT_FOUNT_WITH_ID_MESSAGE = "User with this id not found, id: ";

    public UsersService(UsersRepository usersRepository, UserEntityMapper userEntityMapper) {
        this.usersRepository = usersRepository;
        this.userEntityMapper = userEntityMapper;
    }
    public UserResponse getUserById(Long id) {
        return usersRepository.findById(id)
                .map(userEntityMapper::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUNT_WITH_ID_MESSAGE + id));
    }

    public SaveUserResponse saveUserRequest(Long userId, SaveUserDetailsRequest newUser) {
        var userEntity = userEntityMapper.mapEntityFrom(userId, newUser);
        saveUserEntity(userEntity);
        return new SaveUserResponse(userEntity.getUserId(), userEntity.getLogin());
    }

    public void saveUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        var userEntity = userEntityMapper.mapEntityFrom(userCreatedEvent);
        saveUserEntity(userEntity);
    }

    @Transactional
    public void updateAvatarFromEvent(UserAvatarUpdatedEvent event) {
        var user = usersRepository.findById(event.userId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUNT_WITH_ID_MESSAGE + event.userId()));
        user.setAvatarUrl(event.avatarUrl());
    }

    private void saveUserEntity(UserEntity userEntity) {
        usersRepository.save(userEntity);
    }
}
