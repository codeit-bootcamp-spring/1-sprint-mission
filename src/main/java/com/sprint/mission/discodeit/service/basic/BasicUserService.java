package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.exception.user.InvalidEmailException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest, MultipartFile profile) {
    log.debug("사용자 생성 시작: {}", userCreateRequest);

    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (!isValidEmail(email)) {
      throw InvalidEmailException.withEmail(email);
    }

    if (userRepository.existsByUsername((username))) {
      throw UserAlreadyExistsException.withUsername(username);
    }

    if (userRepository.existsByEmail((email))) {
      throw UserAlreadyExistsException.withEmail(email);
    }

    BinaryContent nullableProfile = Optional.ofNullable(profile)
        .filter(p -> !p.isEmpty())
        .flatMap(this::resolveProfileRequest)
        .map(profileImage -> {
          String fileName = profileImage.fileName();
          String contentType = profileImage.contentType();
          byte[] bytes = profileImage.file();
          BinaryContent binaryContent = new BinaryContent(fileName, contentType,
              (long) bytes.length);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);

    User user = new User(
        userCreateRequest.username(),
        userCreateRequest.email(),
        userCreateRequest.password(),
        nullableProfile,
        null);

    userRepository.save(user);
    UserStatus userStatus = new UserStatus(user, Instant.now());
    userStatusRepository.save(userStatus);
    log.info("사용자 생성 완료: id={}, username={}", user.getId(), username);
    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserDto find(UUID userId) {
    log.debug("사용자 조회 시작: id={}", userId);
    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    log.info("사용자 조회 완료: id={}", userId);
    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserDto> findAll() {
    log.debug("모든 사용자 조회 시작");
    List<UserDto> userList = userRepository.findAll()
        .stream()
        .map(userMapper::toDto)
        .toList();
    log.info("모든 사용자 조회 완료: 총 {}명", userList.size());
    return userList;
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile profile) {
    log.debug("사용자 수정 시작: id={}, request={}", userId, userUpdateRequest);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    String newUsername = userUpdateRequest.newUserName();
    String newEmail = userUpdateRequest.newEmail();

    if (!user.getPassword().equals(userUpdateRequest.password())) {
      throw InvalidCredentialsException.wrongPassword();
    }

    if (!isValidEmail(newEmail)) {
      throw InvalidEmailException.withEmail(newEmail);
    }

    if (userRepository.existsByUsername(newUsername)) {
      throw UserAlreadyExistsException.withUsername(newUsername);
    }

    if (userRepository.existsByEmail(newEmail)) {
      throw UserAlreadyExistsException.withEmail(newEmail);
    }

    Optional<BinaryContent> newProfile = Optional.ofNullable(profile)
        .filter(p -> !p.isEmpty())
        .flatMap(this::resolveProfileRequest)
        .map(profileImage -> {
          String fileName = profileImage.fileName();
          String contentType = profileImage.contentType();
          byte[] bytes = profileImage.file();
          BinaryContent binaryContent = new BinaryContent(fileName, contentType,
              (long) bytes.length);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        });

    newProfile.ifPresent(binaryContent -> {
      if (user.getProfile() != null) {
        binaryContentRepository.deleteById(user.getProfile().getId());
      }
      binaryContentRepository.save(binaryContent);
      user.updateProfile(binaryContent);
    });

    user.update(userUpdateRequest.newUserName(), userUpdateRequest.newEmail());
    userRepository.save(user);
    log.info("사용자 수정 완료: id={}", userId);
    return userMapper.toDto(user);
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile == null || profileFile.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(new BinaryContentCreateRequest(
          profileFile.getOriginalFilename(),
          profileFile.getContentType(),
          profileFile.getBytes()
      ));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.debug("사용자 삭제 시작: id={}", userId);

    userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    userRepository.deleteById(userId);
    log.info("사용자 삭제 완료: id={}", userId);
  }

  private boolean isValidEmail(String email) {
    return email.matches(EMAIL_REGEX);
  }
}
