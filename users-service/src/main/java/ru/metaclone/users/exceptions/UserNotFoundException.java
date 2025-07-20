package ru.metaclone.users.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {

  public static final String CODE = "USER_NOT_FOUND";

  public UserNotFoundException(String message) {
    super(message);
  }

  @Override
  public String getCode() {
    return CODE;
  }

  @Override
  public HttpStatus getStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
