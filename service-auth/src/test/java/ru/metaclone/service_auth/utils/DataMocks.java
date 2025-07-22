package ru.metaclone.service_auth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import static org.apache.logging.log4j.util.LoaderUtil.getClassLoader;

public class DataMocks {
    public static final String CREDENTIALS = loadFileAsString("mocks/credentials.json");
    public static final String CREDENTIALS_UNKNOWN = loadFileAsString("mocks/credentials-unknown.json");
    public static final String USER_DETAILS = loadFileAsString("mocks/user-details.json");
    public static final String USER_CREATED_EVENT = loadFileAsString("mocks/user-created-event.json");


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
