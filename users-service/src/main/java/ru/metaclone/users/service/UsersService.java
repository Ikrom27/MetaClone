package ru.metaclone.users.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import ru.metaclone.users.data.entity.UserEntity;
import ru.metaclone.users.exceptions.UserNotFoundException;
import ru.metaclone.users.mappers.UserEntityMapper;
import ru.metaclone.users.data.requests.UpdateUserRequest;
import ru.metaclone.users.data.response.UserResponse;
import ru.metaclone.users.data.events.UserAvatarUpdatedEvent;
import ru.metaclone.users.data.events.UserCreatedEvent;
import ru.metaclone.users.repository.UsersRepository;

@Service
@AllArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UserEntityMapper userEntityMapper;
    private final RedisCacheManager cacheManager;

    private static final String USER_NOT_FOUNT_WITH_ID_MESSAGE = "User with this id not found, id: ";
    private static final String USERS_CACHE = "users";

    @Cacheable(cacheNames = USERS_CACHE, key = "#userId")
    public UserResponse getUserById(Long userId) {
        var response = usersRepository.findById(userId)
                .map(userEntityMapper::mapToResponse)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUNT_WITH_ID_MESSAGE + userId));
        return response;
    }

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
        updateCache(userEntity);
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
        updateCache(user);
    }

    private void updateCache(UserEntity userEntity) {
        UserResponse response = userEntityMapper.mapToResponse(userEntity);
        Cache cache = cacheManager.getCache(USERS_CACHE);
        if (cache != null) {
            cache.put(userEntity.getUserId(), response);
        }
    }
}
