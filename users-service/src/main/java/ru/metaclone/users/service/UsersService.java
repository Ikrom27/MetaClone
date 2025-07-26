package ru.metaclone.users.service;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.metaclone.users.exceptions.UserNotFoundException;
import ru.metaclone.users.mappers.UserEntityMapper;
import ru.metaclone.users.data.requests.UpdateUserRequest;
import ru.metaclone.users.data.response.UserResponse;
import ru.metaclone.users.data.events.UserAvatarUpdatedEvent;
import ru.metaclone.users.data.events.UserCreatedEvent;
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

    @Cacheable(cacheNames = "users", key = "#userId")
    public UserResponse getUserById(Long userId) {
        var response = usersRepository.findById(userId)
                .map(userEntityMapper::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUNT_WITH_ID_MESSAGE + userId));
        return response;
    }

    @CachePut(cacheNames = "users", key = "#userId")
    @Transactional
    public UserResponse updateUser(Long userId, UpdateUserRequest newUser) {
        var userEntity = usersRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUNT_WITH_ID_MESSAGE + userId));

        if (newUser.firstName() != null) {
            userEntity.setFirstName(newUser.firstName());
        }
        if (newUser.lastName() != null) {
            userEntity.setSecondName(newUser.lastName());
        }
        if (newUser.birthday() != null) {
            userEntity.setBirthday(newUser.birthday());
        }
        if (newUser.gender() != null) {
            userEntity.setGender(newUser.gender().getValue());
        }
        userEntity.setAbout(newUser.about());
        return userEntityMapper.mapToResponse(userEntity);
    }

    public void createNewUserFromEvent(UserCreatedEvent userCreatedEvent) {
        var userEntity = userEntityMapper.mapEntityFrom(userCreatedEvent);
        usersRepository.save(userEntity);
    }

    @Transactional
    public void updateAvatarFromEvent(UserAvatarUpdatedEvent event) {
        var user = usersRepository.findById(event.userId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUNT_WITH_ID_MESSAGE + event.userId()));
        user.setAvatarUrl(event.avatarUrl());
    }
}
