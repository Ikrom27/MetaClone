package ru.metaclone.users.utils;


import ru.metaclone.users.models.entity.UserEntity;
import ru.metaclone.users.models.enums.Gender;
import ru.metaclone.users.models.events.UserCreatedEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.apache.logging.log4j.util.LoaderUtil.getClassLoader;

public class UserFactory {
    public static UserEntity mockExistingUserEntity() {
        return UserEntity.builder()
                .login("existingUser")
                .firstName("Petr")
                .secondName("Petrov")
                .birthday(LocalDate.of(2000, 5, 5))
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
                LocalDate.of(2000, 5, 5),
                Gender.MALE
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

