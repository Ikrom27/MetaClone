package ru.metaclone.users.utils;


import ru.metaclone.users.data.entity.UserEntity;
import ru.metaclone.users.data.enums.Gender;
import ru.metaclone.users.data.events.UserAvatarUpdatedEvent;
import ru.metaclone.users.data.events.UserCreatedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import static org.apache.logging.log4j.util.LoaderUtil.getClassLoader;

public class UserFactory {
    public static UserEntity mockExistingUserEntity() {
        return UserEntity.builder()
                .userId(1L)
                .login("existingUser")
                .firstName("Petr")
                .secondName("Petrov")
                .birthday(OffsetDateTime.parse("2000-05-05T00:00:00Z"))
                .gender("MALE")
                .avatarUrl("http://avatar.new.url")
                .about("about")
                .followsCount(10)
                .followersCount(20)
                .build();
    }

    public static UserCreatedEvent mockUserCreatedEvent() {
        return new UserCreatedEvent(
                123L,
                "existingUser",
                "Petr",
                "Petrov",
                OffsetDateTime.parse("2000-05-05T00:00:00Z"),
                Gender.MALE
        );
    }

    public static UserAvatarUpdatedEvent mockUserAvatarUpdatedEvent() {
        return new UserAvatarUpdatedEvent(
                1L,
                "http://updatedAvatar.ru"
        );
    }


    public static String loadFileAsString(String path) {
        try (InputStream is = getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalArgumentException("Файл не найден: " + path);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Ошибка чтения файла: " + path, e);
        }
    }
}

