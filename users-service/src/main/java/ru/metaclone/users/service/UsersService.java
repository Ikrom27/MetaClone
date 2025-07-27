package ru.metaclone.users.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import ru.metaclone.users.data.entity.SubscriptionEntity;
import ru.metaclone.users.data.entity.UserEntity;
import ru.metaclone.users.data.response.UserPreview;
import ru.metaclone.users.exceptions.SelfFollowException;
import ru.metaclone.users.exceptions.UserNotFoundException;
import ru.metaclone.users.mappers.UserEntityMapper;
import ru.metaclone.users.data.requests.UpdateUserRequest;
import ru.metaclone.users.data.response.UserResponse;
import ru.metaclone.users.data.events.UserAvatarUpdatedEvent;
import ru.metaclone.users.data.events.UserCreatedEvent;
import ru.metaclone.users.repository.SubscriptionRepository;
import ru.metaclone.users.repository.UsersRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersService {
    private final SubscriptionRepository subscriptionRepository;
    private final UsersRepository usersRepository;
    private final UserEntityMapper userEntityMapper;
    private final RedisCacheManager cacheManager;

    private static final String USER_NOT_FOUNT_WITH_ID_MESSAGE = "User with this id not found, id: ";
    private static final String SELF_FOLLOW_MESSAGE = "You cant follow to yourself";
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
            userEntity.setLastName(newUser.lastName());
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

    @Transactional
    public void follow(Long followerId, Long followeeId) throws UserNotFoundException {
        if (followerId.equals(followeeId))
            throw new SelfFollowException(SELF_FOLLOW_MESSAGE);
        if (!usersRepository.existsById(followeeId))
            throw new UserNotFoundException(USER_NOT_FOUNT_WITH_ID_MESSAGE + followeeId);
        if (!usersRepository.existsById(followerId))
            throw new UserNotFoundException(USER_NOT_FOUNT_WITH_ID_MESSAGE + followerId);

        SubscriptionEntity.SubscriptionId subscriptionId =
                new SubscriptionEntity.SubscriptionId(followerId, followeeId);

        if (subscriptionRepository.existsById(subscriptionId)) {
            return;
        }

        SubscriptionEntity subscription = SubscriptionEntity.builder()
                .id(subscriptionId)
                .createdAt(OffsetDateTime.now())
                .build();

        subscriptionRepository.save(subscription);
    }


    public List<UserPreview> getFollowers(Long userId, int page, int size) {
        List<SubscriptionEntity> subscriptions =
                subscriptionRepository.findByIdFolloweeIdOrderByCreatedAtDesc(userId);

        List<Long> followerIds = subscriptions.stream()
                .map(subscription -> subscription.getId().getFollowerId())
                .collect(Collectors.toList());

        if (followerIds.isEmpty()) {
            return List.of();
        }

        return usersRepository.findUserPreviewsByUserIds(followerIds, PageRequest.of(page, size));
    }


    public List<UserPreview> getFollowing(Long userId, int page, int size) {
        List<SubscriptionEntity> subscriptions =
                subscriptionRepository.findByIdFollowerIdOrderByCreatedAtDesc(userId);

        List<Long> followeeIds = subscriptions.stream()
                .map(subscription -> subscription.getId().getFolloweeId())
                .collect(Collectors.toList());

        if (followeeIds.isEmpty()) {
            return List.of();
        }

        return usersRepository.findUserPreviewsByUserIds(followeeIds, PageRequest.of(page, size));
    }

}
