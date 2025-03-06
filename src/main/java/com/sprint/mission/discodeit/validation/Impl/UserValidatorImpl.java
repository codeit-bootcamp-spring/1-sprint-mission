package com.sprint.mission.discodeit.validation.Impl;


import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidatorImpl implements UserValidator {

  private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

  private final UserRepository userRepository;

  @Override
  public boolean isValidName(String name) {
    if (name.isBlank()) {
      throw new RestApiException(ErrorCode.USER_NAME_REQUIRED, "name=" + name);
    } else if (name.length() < 2) {
      throw new RestApiException(ErrorCode.USER_NAME_TOO_SHORT,
          "name=" + name);
    } else if (userRepository.existsByName(name)) {
      throw new RestApiException(ErrorCode.USER_NAME_ALREADY_EXIST,
          "name= " + name);
    }
    return true;
  }

  @Override
  public boolean isValidEmail(String email) {
    if (!email.matches(EMAIL_REGEX)) {
      throw new RestApiException(ErrorCode.USER_EMAIL_FORMAT_NOT_MATCH,
          "email=" + email);
    } else if (userRepository.existsByEmail(email)) {
      throw new RestApiException(ErrorCode.USER_EMAIL_ALREADY_EXIST,
          "email=" + email);
    }
    return true;
  }

  @Override
  public boolean isValidPassword(String password) {
    if (password.isBlank()) {
      throw new RestApiException(ErrorCode.USER_PASSWORD_REQUIRED, "");
    } else if (password.length() < 6) {
      throw new RestApiException(ErrorCode.USER_PASSWORD_TOO_SHORT, "");
    }
    return true;
  }
}
