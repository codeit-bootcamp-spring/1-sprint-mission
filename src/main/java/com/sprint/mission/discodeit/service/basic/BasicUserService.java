package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.UserConstant;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.util.FileTypeProcessor;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static com.sprint.mission.discodeit.constant.UserConstant.*;

@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final EntityValidator validator;

  /**
   * 사용자를 생성하는 메서드.
   *
   * <p>{@link CreateUserDto}를 기반으로 새로운 사용자를 생성하고,
   * 프로필 이미지를 저장한 후, 사용자 상태 정보를 초기화하여 저장.</p>
   *
   * @param userDto 사용자의 정보를 포함하는 DTO
   * @return 생성된 사용자 객체
   * @throws UserValidationException  이메일, 닉네임, 전화번호 검증에 실패할 경우 발생
   * @throws IllegalArgumentException 필수 필드가 누락된 경우 발생
   */
  @Override
  public User createUser(CreateUserDto userDto) {

    User user = new User.UserBuilder(
        userDto.username(),
        PasswordEncryptor.hashPassword(userDto.password()),
        userDto.email(),
        userDto.phoneNumber()
    )
        .nickname(userDto.nickname())
        .description(userDto.description())
        .build();

    validEmail(userDto.email(), user.getUUID());
    validNickname(userDto.nickname(), user.getUUID());
    validPhone(userDto.phoneNumber(), user.getUUID());

    saveProfileImage(user, userDto.imageName(), userDto.fileType(), userDto.profileImage());

    UserStatus status = new UserStatus(user.getUUID(), Instant.now());

    userRepository.create(user);
    userStatusRepository.save(status);

    return user;
  }

  /**
   * 범용성이 높도록 Dto 를 인자로 받지 않음
   * file 이 비어있으면 동작하지 않음
   *
   * @param user      사용자 객체
   * @param imageName 넘어온 이미지 이름
   * @param fileType  파일의 종류 (FileTypeProcessor 로 Enum 으로 변환)
   * @param file      실제 파일
   */
  private void saveProfileImage(User user, String imageName, String fileType, byte[] file) {
    if (file != null && file.length != 0) {
      //TODO : 필드 예외 처리
      BinaryContent profileImage = new BinaryContent.BinaryContentBuilder(
          user.getUUID(),
          imageName,
          FileTypeProcessor.process(fileType),
          file.length,
          file
      ).build();

      user.setBinaryContentId(profileImage.getUUID());
      binaryContentRepository.save(profileImage);
    }
  }


  private void validEmail(String email, String id) {
    if (!email.matches(EMAIL_REGEX)) throw new UserValidationException(ERROR_INVALID_EMAIL);
    List<User> users = userRepository.findAll();
    if (users.stream()
        .anyMatch(u -> u.getEmail().equals(email) && !u.getUUID().equals(id)))
      throw new UserValidationException(DUPLICATE_EMAIL);
  }

  private void validPhone(String phoneNumber, String id) {
    if (!phoneNumber.matches(PHONE_REGEX)) throw new UserValidationException(ERROR_INVALID_PHONE);
    List<User> users = userRepository.findAll();
    if (users.stream()
        .anyMatch(u -> u.getPhoneNumber().equals(phoneNumber) && !u.getUUID().equals(id)))
      throw new UserValidationException(DUPLICATE_PHONE);
  }

  private void validNickname(String nickname, String id) {
    if (nickname.length() <= UserConstant.USERNAME_MIN_LENGTH
        || nickname.length() > UserConstant.USERNAME_MAX_LENGTH) {
      throw new UserValidationException(UserConstant.ERROR_USERNAME_LENGTH);
    }
    List<User> users = userRepository.findAll();
    if (users.stream()
        .anyMatch(u -> u.getNickname().equals(nickname) && !u.getUUID().equals(id)))
      throw new UserValidationException(DUPLICATE_NICKNAME);
  }

  /**
   * 사용자를 찾아서 반환
   * id 에 해당하는 사용자가 없다면 UserNotFoundException
   * id 에 해당하는 status 가 없다면 새로운 status 생성
   *
   * @param id
   */
  @Override
  public UserResponseDto findUserById(String id) {

    User user = validator.findOrThrow(User.class, id, new UserNotFoundException());

    UserStatus status = userStatusRepository.findByUserId(id).orElseGet(
        () -> createStatusIfNotExists(id)
    );

    return UserResponseDto.from(user, status);
  }

  private UserStatus createStatusIfNotExists(String id) {
    UserStatus newStatus = new UserStatus(id, Instant.now());
    userStatusRepository.save(newStatus);
    return newStatus;
  }

  @Override
  public List<UserResponseDto> findAllUsers() {

    List<User> users = userRepository.findAll();

    return users.stream()
        .map(user -> UserResponseDto.from(
            user,
            userStatusRepository.findByUserId(user.getUUID())
                .orElseGet(
                    () -> createStatusIfNotExists(user.getUUID())
                )
        )).toList();
  }

  @Override
  public void updateUser(String id, UserUpdateDto updatedUser, String originalPassword) {
    User originalUser = validator.findOrThrow(User.class, id, new UserNotFoundException());

    if (!PasswordEncryptor.checkPassword(originalPassword, originalUser.getPassword()))
      throw new UserValidationException(PASSWORD_MATCH_ERROR);

    synchronized (originalUser) {
      if (updatedUser.nickname() != null) {
        validNickname(updatedUser.nickname(), id);
        originalUser.setNickname(updatedUser.nickname());
      }
      if (updatedUser.email() != null) {
        validEmail(updatedUser.email(), id);
        originalUser.setEmail(updatedUser.email());
      }
      if (updatedUser.phoneNumber() != null) {
        validPhone(updatedUser.phoneNumber(), id);
        originalUser.setPhoneNumber(updatedUser.phoneNumber());
      }
      if (updatedUser.username() != null) {
        originalUser.setUsername(updatedUser.username());
      }
      if (updatedUser.description() != null) {
        originalUser.setDescription(updatedUser.description());
      }
      if (updatedUser.password() != null) {
        originalUser.setPassword(PasswordEncryptor.hashPassword(updatedUser.password()));
      }
    }

    saveProfileImage(originalUser, updatedUser.imageName(), updatedUser.fileType(), updatedUser.profileImage());
    userRepository.update(originalUser);
  }

  @Override
  public void deleteUser(String id, String password) {

    User user = validator.findOrThrow(User.class, id, new UserNotFoundException());

    if (!PasswordEncryptor.checkPassword(password, user.getPassword()))
      throw new UserValidationException(PASSWORD_MATCH_ERROR);
    userRepository.delete(id);
    userStatusRepository.deleteByUserId(id);
    binaryContentRepository.deleteByUserId(id);
  }
}
