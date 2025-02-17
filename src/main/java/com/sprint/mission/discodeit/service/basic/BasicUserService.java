package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.UserConstant;
import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user_status.CreateUserStatusDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.UserConstant.*;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final EntityValidator validator;

  @Override
  public User saveUser(User user) {

    // email, nickname, phone number 검증
    validateUserInformationWhenCreate(user, user.getUUID());

    // user 생성
    return userRepository.create(user);
  }

  @Override
  public User update(User user){
    return userRepository.update(user);
  }

  private void validateUserInformationWhenCreate(User user, String userId) {
    List<User> users = userRepository.findAll();
    validEmail(user.getEmail(), userId, users);
    validNickname(user.getNickname(), userId, users);
    validPhone(user.getPhoneNumber(), userId, users);
  }

  private void validEmail(String email, String id, List<User> users) {
    if (!email.matches(EMAIL_REGEX)) throw new UserValidationException(ERROR_INVALID_EMAIL);
    if (users.stream()
        .anyMatch(u -> u.getEmail().equals(email) && !u.getUUID().equals(id)))
      throw new UserValidationException(DUPLICATE_EMAIL);
  }

  private void validPhone(String phoneNumber, String id, List<User> users) {
    if (!phoneNumber.matches(PHONE_REGEX)) throw new UserValidationException(ERROR_INVALID_PHONE);
    if (users.stream()
        .anyMatch(u -> u.getPhoneNumber().equals(phoneNumber) && !u.getUUID().equals(id)))
      throw new UserValidationException(DUPLICATE_PHONE);
  }

  private void validNickname(String nickname, String id, List<User> users) {
    if (nickname.length() <= UserConstant.USERNAME_MIN_LENGTH
        || nickname.length() > UserConstant.USERNAME_MAX_LENGTH) {
      throw new UserValidationException(UserConstant.ERROR_USERNAME_LENGTH);
    }
    if (users.stream()
        .anyMatch(u -> u.getNickname().equals(nickname) && !u.getUUID().equals(id)))
      throw new UserValidationException(DUPLICATE_NICKNAME);
  }

  /**
   * 사용자를 찾아서 반환
   * id 에 해당하는 사용자가 없다면 UserNotFoundException
   * id 에 해당하는 status 가 없다면 새로운 status 생성
   */
  @Override
  public User findUserById(String id) {
    return validator.findOrThrow(User.class, id, new UserNotFoundException());
  }

  @Override
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }


  /**
   * 사용자 정보를 업데이트 한다
   * <p>
   *   <ul>
   *     <li>id로 사용자를 찾는다. 없을 경우 {@link UserNotFoundException}</li>
   *     <li>입력받은 비밀번호와 기존 비빌번호를 비교</li>
   *     <li>사용자의 필드를 업데이트</li>
   *     <li>프로필 이미지 업데이트 및 사용자 레포지토리 반영</li>
   *   </ul>
   * </p>
   *
   * @param id            업데이트 할 사용자 id
   * @param updatedUser   업데이트 정보가 담긴 DTO
   * @param plainPassword 사용자가 입력한 비밀번호
   */
  @Override
  public User updateUser(String id, UserUpdateDto updatedUser, String plainPassword) {

    User originalUser = validator.findOrThrow(User.class, id, new UserNotFoundException());

    checkPasswordIsCorrect(plainPassword, originalUser.getPassword());

    synchronized (originalUser) {
      updateFields(originalUser, updatedUser);
    }

    return userRepository.update(originalUser);
  }

  /**
   * 비밀번호에 대한 검증
   * 일치하지 않을 경우 {@link UserValidationException}
   *
   * @param plain  사용자가 입력한 비밀번호
   * @param hashed 기존에 저장되어 있던 비밀번호
   */
  private void checkPasswordIsCorrect(String plain, String hashed) {
    if (!PasswordEncryptor.checkPassword(plain, hashed))
      throw new UserValidationException(PASSWORD_MATCH_ERROR);
  }

  /**
   * {@link UserUpdateDto} 기반으로 사용자 필드를 업데이트
   * <p>
   *   <ul>
   *     <li>사용자 전체 목록 조회 후, nickname, email, phonenumber 중복 여부 검증</li>
   *     <li>각 필드가 null 이 아닌 경우만 update</li>
   *     <li>비밀번호는 해시화 하여 저장</li>
   *   </ul>
   * </p>
   *
   * @param originalUser 업데이트 대상 User
   * @param updatedUser  업데이트 될 정보
   */
  private void updateFields(User originalUser, UserUpdateDto updatedUser) {

    List<User> users = userRepository.findAll();

    if (updatedUser.nickname() != null) {
      validNickname(updatedUser.nickname(), originalUser.getUUID(), users);
      originalUser.setNickname(updatedUser.nickname());
    }
    if (updatedUser.email() != null) {
      validEmail(updatedUser.email(), originalUser.getUUID(), users);
      originalUser.setEmail(updatedUser.email());
    }
    if (updatedUser.phoneNumber() != null) {
      validPhone(updatedUser.phoneNumber(), originalUser.getUUID(), users);
      originalUser.setPhoneNumber(updatedUser.phoneNumber());
    }
    if (updatedUser.username() != null) {
      originalUser.setUsername(updatedUser.username());
    }
    if (updatedUser.description() != null) {
      originalUser.setDescription(updatedUser.description());
    }
    if (updatedUser.password() != null && !updatedUser.password().isBlank()) {
      originalUser.setPassword(PasswordEncryptor.hashPassword(updatedUser.password()));
    }
  }

  /**
   * 사용자를 조회하여 비밀번호 검증 후 삭제
   * 관련된 {@link UserStatus} {@link BinaryContent} 도 같이 삭제
   *
   * @param id
   * @param password
   */
  @Override
  public void deleteUser(String id, String password) {
    User user = validator.findOrThrow(User.class, id, new UserNotFoundException());


    checkPasswordIsCorrect(password, user.getPassword());

    userRepository.delete(id);
  }
}
