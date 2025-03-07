package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.userStatus.CreateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.status.AccountStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.TypeMismatchException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserDto create(CreateUserDto createUserDto) {

    boolean userEmailExists =
        userRepository.findByEmail(createUserDto.email()).orElse(null) != null;
    if (userEmailExists) {
      throw new CustomException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
    }

    boolean userNameExists =
        userRepository.findByUsername(createUserDto.username()).orElse(null) != null;
    if (userNameExists) {
      throw new CustomException(ErrorCode.USER_NAME_ALREADY_REGISTERED);
    }

    if (createUserDto == null || createUserDto.username() == null
        || createUserDto.password() == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    //todo - User 생성 시 UserStatus 생성하는 로직 리팩터링 필요
    // 현재 구조: user 생성(userstatus 필드 비어있음) -> userStatus 생성(user가지고) 후 userStatusDto 받음
    // -> userStatusDto에 있는 id로 userStatus 다시 조회 -> 조회 된 userStatus를 user에 set
    // 비효율적인것 같다...
    User user = new User(createUserDto.username(), createUserDto.nickname(), createUserDto.email(),
        createUserDto.password(), null, AccountStatus.UNVERIFIED, null);

    UserStatusDto userStatusDto = userStatusService.create(
        new CreateUserStatusDto(user.getId().toString()));

    UserStatus userStatus = userStatusRepository.findByUser(user).orElse(null);
    user.setUserStatus(userStatus);

    userRepository.save(user);
    return UserDto.from(user, user.getUserStatus().isActive());
  }

  @Override
  public UserDto create(CreateUserDto createUserDto, MultipartFile file)
      throws CustomException {
    UserDto userDto = create(createUserDto);
    User user = userRepository.findById(userDto.id()).orElse(null);

    if (!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpeg") &&
        !file.getContentType().equals("image/gif") && !file.getContentType().equals("image/jpg")) {
      throw new TypeMismatchException("Image type only supported");
    }
    BinaryContentDto binaryContentDto = binaryContentService.create(file);
    BinaryContent profile = binaryContentRepository.findById(binaryContentDto.id()).orElse(null);
    user.setProfile(profile);
    userRepository.save(user);

    return UserDto.from(user, user.getUserStatus().isActive());
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll().stream()
        .map(u -> UserDto.from(u, u.getUserStatus().isActive()))
        .toList();
  }

  @Override
  public UserDto findById(String userId) throws CustomException {
    if (userId == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "USER ID is null");
    }
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);

    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND,
          String.format("User with id %s not found", userId));
    }
    return UserDto.from(user, user.getUserStatus().isActive());
  }

  @Override
  public UserDto findByEmail(String email) throws CustomException {
    User user = userRepository.findAll().stream().filter(u -> u.getEmail().equals(email))
        .findFirst().orElse(null);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND,
          String.format("User with email %s not found", email));
    }
    return UserDto.from(user, user.getUserStatus().isActive());
  }

  @Override
  public List<UserDto> findAllContainsNickname(String nickname) {
    return userRepository.findAll().stream()
        .filter(user -> user.getNickname().contains(nickname))
        .map(
            user -> UserDto.from(user, user.getUserStatus().isActive())).toList();
  }

  @Override
  public List<UserDto> findAllByAccountStatus(AccountStatus accountStatus) {
    return userRepository.findByAccountStatus(accountStatus)
        .stream()
        .map(u -> UserDto.from(u, u.getUserStatus().isActive())).toList();
  }

  @Override
  public UserDto updateUser(String userId, UpdateUserDto updateUserDto)
      throws CustomException {

    if (updateUserDto == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "USER DTO is null");
    }

    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);

    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND,
          String.format("User with id %s not found", userId));
    }

    if (user.isUpdated(updateUserDto)) {
      user.setUpdatedAt(updateUserDto.updatedAt());
    }

    User savedUser = userRepository.save(user);
    UserStatusDto userStatusDto = userStatusService.updateByUserId(userId,
        new UpdateUserStatusDto(Instant.now()));

    return UserDto.from(savedUser, savedUser.getUserStatus().isActive());
  }

  // 선택적으로 프로필 이미지를 대체할 수 있도록 하는 메서드
  @Override
  public UserDto updateUser(String userId, UpdateUserDto updateUserDto, MultipartFile file)
      throws CustomException {
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
    //todo - 유저 조회를 두 번 한다. 수정 필요
    updateUser(userId, updateUserDto);

    if (file == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    if (user.getProfile() != null) {
      binaryContentRepository.delete(user.getProfile().getId());
    }
    BinaryContentDto binaryContentDto = binaryContentService.create(file);

    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentDto.id())
        .orElse(null);

    user.setProfile(binaryContent);
    user.setUpdatedAt(updateUserDto.updatedAt());

    UserStatusDto userStatusDto = userStatusService.updateByUserId(userId,
        new UpdateUserStatusDto(Instant.now()));

    User savedUser = userRepository.save(user);

    return UserDto.from(savedUser, savedUser.getUserStatus().isActive());
  }


  @Override
  public boolean deleteUser(String userId) throws CustomException {
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);

    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND,
          String.format("User with id %s not found", userId));
    }

    //todo - userStatus가 삭제되지 않았다면?
    boolean delete = userStatusService.delete(user.getUserStatus().getId().toString());
    if (!delete) {
      //todo - errorcode 수정, 예외처리 전체 수정
      throw new CustomException(ErrorCode.NOT_DELETED);
    }
    userRepository.delete(user);

    return true;
  }
}
