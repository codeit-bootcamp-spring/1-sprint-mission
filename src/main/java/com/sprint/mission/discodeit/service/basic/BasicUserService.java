package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.code.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.status.AccountStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.TypeMismatchException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  //todo - 고민
  // 자기자신의 메서드 호출을 가로채지 않아서 내부 메서드의 transactional이 무시된다...
  // 그럼 밑의 동일 이름의 create에서 이 create를 호출하면 무시되나?
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

    User user = new User(createUserDto.username(), createUserDto.nickname(), createUserDto.email(),
        createUserDto.password(), null, AccountStatus.UNVERIFIED, null);

    UserStatus userStatus = new UserStatus(user);
    userStatusRepository.save(userStatus);
    user.setUserStatus(userStatus);

    userRepository.save(user);

    return userMapper.toDto(user);
  }

  @Override
  @Transactional
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

    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    return userRepository.findAll().stream().map(userMapper::toDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findById(String userId) throws CustomException {
    if (userId == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA, "USER ID is null");
    }
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);

    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND,
          String.format("User with id %s not found", userId));
    }
    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findByEmail(String email) throws CustomException {
    User user = userRepository.findAll().stream().filter(u -> u.getEmail().equals(email))
        .findFirst().orElse(null);
    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND,
          String.format("User with email %s not found", email));
    }
    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAllContainsNickname(String nickname) {
    return userRepository.findAll().stream()
        .filter(user -> user.getNickname().contains(nickname))
        .map(userMapper::toDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAllByAccountStatus(AccountStatus accountStatus) {
    return userRepository.findByAccountStatus(accountStatus)
        .stream()
        .map(userMapper::toDto).toList();
  }

  @Override
  @Transactional
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

    user.setUsername(updateUserDto.newUsername());
    user.setNickname(updateUserDto.newNickname());
    user.setEmail(updateUserDto.newEmail());
    user.setPassword(updateUserDto.newPassword());
    user.setUpdatedAt(updateUserDto.updatedAt());
    user.setAccountStatus(updateUserDto.accountStatus());
    user.setStatusMessage(updateUserDto.newStatusMessage());

    User savedUser = userRepository.save(user);
    UserStatus userStatus = userStatusRepository.findByUser(savedUser).orElse(null);
    if (userStatus == null) {
      throw new CustomException(ErrorCode.USER_STATUS_NOT_FOUND);
    }
    user.setUserStatus(userStatus);

    return userMapper.toDto(savedUser);
  }

  // 선택적으로 프로필 이미지를 대체할 수 있도록 하는 메서드
  @Override
  @Transactional
  public UserDto updateUser(String userId, UpdateUserDto updateUserDto, MultipartFile file)
      throws CustomException {
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
    //todo - 유저 조회를 두 번 한다. 수정 필요
    updateUser(userId, updateUserDto);

    if (file == null) {
      throw new CustomException(ErrorCode.EMPTY_DATA);
    }

    if (user.getProfile() != null) {

      binaryContentRepository.delete(user.getProfile());
    }
    BinaryContentDto binaryContentDto = binaryContentService.create(file);

    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentDto.id())
        .orElse(null);

    user.setProfile(binaryContent);
    user.setUpdatedAt(updateUserDto.updatedAt());

    UserStatus userStatus = user.getUserStatus();
    userStatus.setUpdatedAt(updateUserDto.updatedAt());

    userStatusRepository.save(userStatus);
    userRepository.save(user);

    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public boolean deleteUser(String userId) throws CustomException {
    User user = userRepository.findById(UUID.fromString(userId)).orElse(null);

    if (user == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND,
          String.format("User with id %s not found", userId));
    }
    userRepository.delete(user);
    return true;
  }
}
