package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.constant.UserConstant;
import com.sprint.mission.discodeit.dto.binary_content.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
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
import com.sprint.mission.discodeit.util.FileTypeProcessor;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;
import static com.sprint.mission.discodeit.constant.UserConstant.*;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;

  private final EntityValidator validator;

  /**
   * 사용자를 생성하는 메서드.
   *
   * <p>{@link CreateUserRequest}를 기반으로 새로운 사용자를 생성하고,
   * 프로필 이미지를 저장한 후, 사용자 상태 정보를 초기화하여 저장.</p>
   *
   * <p>username, email 이 다른 유저와 같다면 예외 발생</p>
   *
   * <p>saveProfileImage() 는 실제 profileImage 가 비어있다면 동작하지 않음.</p>
   *
   * <p>{@link UserStatus} : 사용자의 온라인 정보 생성</p>
   *
   * <p>순서 : user validation -> user repository -> UserStatus 생성 -> userProfileImage 등록 (이때 user update )</p>
   *
   * @param userDto 사용자의 정보를 포함하는 DTO
   * @return 생성된 사용자 객체
   * @throws UserValidationException  이메일, 닉네임, 전화번호 검증에 실패할 경우 발생
   * @throws IllegalArgumentException 필수 필드가 누락된 경우 발생
   */
  @Override
  public UserResponseDto createUser(CreateUserRequest userDto) {

    // User 객체 생성
    User user = createUserDtoToUser(userDto);

    // email, nickname, phone number 검증
    validateUserInformationWhenCreate(user, user.getUUID());

    // user 생성
    userRepository.create(user);

    // UserStatus 생성
    UserStatus status = userStatusService.create(new CreateUserStatusDto(user.getUUID(), Instant.now()));
    BinaryContent userProfileImage = null;

    try {
      // 프로필 이미지가 같이 넘어왔다면 등록
      if (userDto.profileImage() != null) {
        userProfileImage = saveProfileImage(user, userDto.profileImage().getName(), userDto.profileImage().getContentType(), userDto.profileImage().getBytes());
      }
    } catch (IOException e) {
      throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
    }

    return UserResponseDto.from(
        user,
        status,
        userProfileImage
    );
  }

  /**
   * 단순히 UserDto 를 User 객체로 파싱하는 함수
   *
   * @param userDto
   * @return User 객체
   */
  private User createUserDtoToUser(CreateUserRequest userDto) {
    return new User.UserBuilder(
        userDto.username(),
        PasswordEncryptor.hashPassword(userDto.password()),
        userDto.email(),
        userDto.phoneNumber()
    )
        .nickname(userDto.nickname())
        .description(userDto.description())
        .build();
  }

  /**
   * 단순 검증 로직 한번 실행 함수
   */
  private void validateUserInformationWhenCreate(User user, String userId) {
    List<User> users = userRepository.findAll();
    validEmail(user.getEmail(), userId, users);
    validNickname(user.getNickname(), userId, users);
    validPhone(user.getPhoneNumber(), userId, users);
  }

  /**
   * 범용성이 높도록 Dto 를 인자로 받지 않음
   * file 이 비어있으면 동작하지 않음
   *
   * <p>file 이 존재한다면, BinaryContent를 생성해서, user의 binaryContentId (ProfilePicture) 에 할당, binaryContentRepository에 해당 이미지 저장</p>
   * {@link CreateBinaryContentDto} 의 마지막 boolean 필드는 프로필 이미지 여부
   * {@link BinaryContentServiceImpl} 의 create 에서 boolean 여부로
   *
   * @param user      사용자 객체
   * @param imageName 넘어온 이미지 이름
   * @param fileType  파일의 종류 (FileTypeProcessor 로 Enum 으로 변환)
   * @param file      실제 파일
   */
  private BinaryContent saveProfileImage(User user, String imageName, String fileType, byte[] file) {
    if (file != null && file.length != 0) {
      //TODO : 필드 예외 처리

      if (user.getProfileImage() != null) {
        binaryContentService.delete(user.getProfileImage().getUUID());
      }

      CreateBinaryContentDto profileDto = new CreateBinaryContentDto(
          user.getUUID(),
          null,
          imageName,
          fileType,
          file.length,
          file,
          true
      );

      BinaryContent binary = binaryContentService.create(profileDto);
      user.setProfileImage(binary);
      userRepository.update(user);

      return binary;
    }

    return null;
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
  public UserResponseDto findUserById(String id) {

    User user = validator.findOrThrow(User.class, id, new UserNotFoundException());

    UserStatus status = getUserStatusOrCreate(id);

    BinaryContent profilePicture = user.getProfileImage();

    return UserResponseDto.from(user, status, profilePicture);
  }

  /**
   * userStatus 를 가져오거나 없다면 생성
   */
  private UserStatus getUserStatusOrCreate(String userId) {
    return userStatusService.findByUserId(userId);
  }

  /**
   * 프로필 사진이 없다면 null 반환
   * 이떄 프로필 사진 id 는 user 객체에 저장되어 있다
   * 기본 이미지는 프론트에서 처리한다 >> 서버는 null 을 반환
   */
  private BinaryContent getProfilePicture(String binaryContentId) {
    if (binaryContentId == null || binaryContentId.isEmpty()) {
      return null;
    }

    try {
      return binaryContentService.find(binaryContentId);
    } catch (InvalidOperationException e) {
      log.warn("Invalid binaryContentId: {}", binaryContentId);
      return null;
    }
  }

  /**
   * 모든 사용자 정보를 조회하여 UserResponseDto 리스트로 반환
   *
   * <p>내부 로직 :
   *   <ul>
   *     <li>모든 사용자 조회</li>
   *     <li>사용자 UUID Set 추출</li>
   *     <li>UserId에 대해 UserStatus 및 BinaryContent 매핑</li>
   *     <li>각 사용자에 대해 UserStatus 가 없다면 생성</li>
   *   </ul>
   * </p>
   *
   * @return 전체 사용자의 대한 {@link  UserResponseDto} 리스트
   */
  @Override
  public List<UserResponseDto> findAllUsers() {

    List<User> users = userRepository.findAll();

    Set<String> userIdSet = mapToUserUuids(users);

    Map<String, UserStatus> userStatusMap = userStatusService.mapUserToUserStatus(userIdSet);

    Map<String, BinaryContent> binaryContentMap = binaryContentService.mapUserToBinaryContent(userIdSet);

    return createMultipleUserResponses(users, userStatusMap, binaryContentMap);
  }

  /**
   * 주어진 User 리스트에서 UUID 만 추출하여 Set 으로 변환
   *
   * @param users 유저 리스트
   * @return 유저 ID Set
   */
  private Set<String> mapToUserUuids(List<User> users) {
    return users.stream()
        .map(User::getUUID)
        .collect(Collectors.toSet());
  }

  /**
   * 주어진 사용자 리스트를 순회하여 각 사용자의 UserStatus 와 BinaryContent 를 이용해
   * {@link UserResponseDto}  생성
   *
   * <p>
   * UserStatus 가 존재하지 않을 경우 생성
   * </p>
   *
   * @param users            사용자 리스트
   * @param userStatusMap    사용자ID : 사용자 UserStatus
   * @param binaryContentMap 사용자ID : 사용자 BinaryContent
   * @return {@link UserResponseDto} 리스트
   */
  private List<UserResponseDto> createMultipleUserResponses(
      List<User> users,
      Map<String, UserStatus> userStatusMap,
      Map<String, BinaryContent> binaryContentMap
  ) {
    return users.stream()
        .map(user -> {
          UserStatus userStatus = getOrCreateUserStatus(userStatusMap, user.getUUID());
          BinaryContent profilePicture = binaryContentMap.getOrDefault(user.getUUID(), null);
          return UserResponseDto.from(user, userStatus, profilePicture);
        }).toList();
  }

  /**
   * 사용자에게 할당된 UserStatus 가 없다면 생성
   *
   * @param userStatusMap UserStatus Map
   * @param userId        사용자 id
   * @return 기존에 존재하던 UserStatus 혹은 생성한 user status
   */
  private UserStatus getOrCreateUserStatus(Map<String, UserStatus> userStatusMap, String userId) {
    return userStatusMap.containsKey(userId)
        ? userStatusMap.get(userId)
        : userStatusService.create(new CreateUserStatusDto(userId, Instant.now()));
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
  public void updateUser(String id, UserUpdateDto updatedUser, String plainPassword) {

    User originalUser = validator.findOrThrow(User.class, id, new UserNotFoundException());

    checkPasswordIsCorrect(plainPassword, originalUser.getPassword());

    synchronized (originalUser) {
      updateFields(originalUser, updatedUser);
    }

    saveProfileImage(originalUser, updatedUser.imageName(), updatedUser.fileType(), updatedUser.profileImage());
    userRepository.update(originalUser);
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
    if (updatedUser.password() != null) {
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
    userStatusService.deleteByUserId(id);
    if (user.getProfileImage() != null) {
      binaryContentService.delete(user.getProfileImage().getUUID());
    }
  }
}
