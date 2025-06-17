package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.event.UserRoleChangedEvent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final ApplicationEventPublisher eventPublisher; // 이벤트 발행

  @CachePut(value = "users", key = "#result.id")
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  //todo - 고민
  // 자기자신의 메서드 호출을 가로채지 않아서 내부 메서드의 transactional이 무시된다...
  // 그럼 밑의 동일 이름의 create에서 이 create를 호출하면 무시되나?
  public UserDto create(CreateUserDto createUserDto) {
    log.info("Create user {}", createUserDto);

    boolean userEmailExists =
        userRepository.findByEmail(createUserDto.email()).orElse(null) != null;
    if (userEmailExists) {
      log.warn("이메일 중복: {}", createUserDto.email());
      throw new UserAlreadyExistException(ErrorCode.USER_EMAIL_ALREADY_REGISTERED);
    }

    boolean userNameExists =
        userRepository.findByUsername(createUserDto.username()).orElse(null) != null;
    if (userNameExists) {
      log.warn("사용자명 중복: {}", createUserDto.username());
      throw new UserAlreadyExistException(ErrorCode.USER_NAME_ALREADY_REGISTERED);
    }

    if (createUserDto == null || createUserDto.username() == null
        || createUserDto.password() == null) {
      log.error("사용자 생성 데이터 누락: CreateUserDto = {}", createUserDto);
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    User user = new User(createUserDto.username(), createUserDto.email(),
        passwordEncoder.encode(createUserDto.password()), null);
    userRepository.save(user);

    log.info("사용자 생성 완료: id = {}, email = {}, username = {}", user.getId(), user.getEmail(),
        user.getUsername());

    return userMapper.toDto(user);
  }

  @CachePut(value = "users", key = "#result.id")
  @Override
  @Transactional
  public UserDto create(CreateUserDto createUserDto, MultipartFile file)
      throws DiscodeitException {
    UserDto userDto = create(createUserDto);
    User user = userRepository.findById(userDto.id())
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    log.info("사용자 생성 후 프로필 추가: userId={}", user.getId());
    if (!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpeg") &&
        !file.getContentType().equals("image/gif") && !file.getContentType().equals("image/jpg")) {
      log.error("사용자 프로필 사진 확장자 오류");
      throw new TypeMismatchException("Image type only supported");
    }
    BinaryContentDto binaryContentDto = binaryContentService.create(file);
    BinaryContent profile = binaryContentRepository.findById(binaryContentDto.id()).orElse(null);
    if (profile == null) {
      log.warn("사용자 프로필 사진 누락");
      userRepository.save(user);
    } else {
      user.setProfile(profile);
      userRepository.save(user);
      log.info("사용자 프로필 사진 등록: id = {}, profile = {}", user.getId(), profile.getId());
    }
    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    return userRepository.findAll().stream().map(userMapper::toDto).toList();
  }

  @Cacheable(value = "users", key = "#userId")
  @Override
  @Transactional(readOnly = true)
  public UserDto findById(String userId) throws DiscodeitException {
    if (userId == null) {
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }
    User user = userRepository.findById(UUID.fromString(userId))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    return userMapper.toDto(user);
  }

  @CachePut(value = "users", key = "#userId")
  @Override
  @Transactional
  public UserDto updateUser(String userId, UpdateUserDto updateUserDto)
      throws DiscodeitException {
    log.info("사용자 수정: userId = {}, updateUserDto = {}", userId, updateUserDto);

    if (updateUserDto == null) {
      log.error("사용자 수정 데이터 누락");
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    User user = userRepository.findById(UUID.fromString(userId))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    user.setUsername(
        updateUserDto.newUsername() == null ? user.getUsername() : updateUserDto.newUsername());
    user.setEmail(updateUserDto.newEmail() == null ? user.getEmail() : updateUserDto.newEmail());

    user.setPassword(
        updateUserDto.newPassword() == null ? user.getPassword()
            : passwordEncoder.encode(updateUserDto.newPassword()));

    user.setUpdatedAt(
        updateUserDto.updatedAt() == null ? Instant.now() : updateUserDto.updatedAt());

    User savedUser = userRepository.save(user);
    log.info("사용자 수정 완료: userId = {}", savedUser.getId());

    userRepository.save(user);

    return userMapper.toDto(savedUser);
  }

  // 선택적으로 프로필 이미지를 대체할 수 있도록 하는 메서드
  @CachePut(value = "users", key = "#userId")
  @Override
  @Transactional
  public UserDto updateUser(String userId, UpdateUserDto updateUserDto, MultipartFile file)
      throws DiscodeitException {
    log.info("사용자 프로필 사진과 함께 수정: userId = {}, updateUserDto = {} ", userId, updateUserDto);
    User user = userRepository.findById(UUID.fromString(userId))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));
    //todo - 유저 조회를 두 번 한다. 수정 필요
    updateUser(userId, updateUserDto);

    if (file == null) {
      log.error("프로필 사진 누락");
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    if (user.getProfile() != null) {
      log.info("사용자 이전 프로필 사진 삭제: userId = {}, profile = {}", userId,
          user.getProfile().getId());
      binaryContentRepository.delete(user.getProfile());
      log.debug("사용자 이전 프로필 삭제 완료");
    }
    log.debug("사용자 프로필 사진 등록: userid = {}", user.getId());
    BinaryContentDto binaryContentDto = binaryContentService.create(file);

    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentDto.id())
        .orElse(null);

    user.setProfile(binaryContent);
    log.debug("사용자 프로필 사진 등록 완료: userId = {}, profile = {}", user.getId(),
        user.getProfile().getId());

    user.setUpdatedAt(updateUserDto.updatedAt());

    userRepository.save(user);
    log.info("사용자 수정 완료: userId = {}", userId);

    return userMapper.toDto(user);
  }

  @Caching(evict = {
      @CacheEvict(value = "users", key = "#userId"),
      @CacheEvict(value = "userChannels", key = "#userId"),
      @CacheEvict(value = "userNotifications", key = "#userId"),
      @CacheEvict(value = "channelParticipants", allEntries = true)
  })
  @Override
  @Transactional
  public boolean deleteUser(String userId) throws DiscodeitException {
    log.info("사용자 삭제 시작: userId = {}", userId);

    User user = userRepository.findById(UUID.fromString(userId))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    userRepository.delete(user);
    log.info("사용자 삭제 완료: userId = {}", userId);
    return true;
  }


  @CacheEvict(value = "users", key = "#roleUpdateRequest.userId")
  @Transactional
  @Override
  public UserDto updateUserRole(RoleUpdateRequest roleUpdateRequest) {

    log.info("사용자 권한 변경 시작: userId = {}, role = {}", roleUpdateRequest.getUserId(),
        roleUpdateRequest.getNewRole().toString());

    if (roleUpdateRequest.getUserId() == null || roleUpdateRequest.getNewRole() == null) {
      throw new IllegalArgumentException("Empty Data");
    }

    User user = userRepository.findById(roleUpdateRequest.getUserId())
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    Role previousRole = user.getRole();

    user.setRole(roleUpdateRequest.getNewRole());
    userRepository.save(user);
    log.info("사용자 권한 변경 완료: userId = {}, role = {}", user.getId(), user.getRole());

    log.debug("사용자 권한 변경 후 세션 종료를 위한 이벤트 호출");
    jwtService.invalidateJwtSession(user.getId());

    log.debug("사용자 권한 변경 후 알림 발행을 위한 이벤트 호출");
    eventPublisher.publishEvent(
        new UserRoleChangedEvent(
            user.getId(), user.getId(), roleUpdateRequest.getNewRole().toString()));

    return userMapper.toDto(user);
  }

}
