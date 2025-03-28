package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.log.PrivacyMaskingUtil;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();
    log.info("[UserService] 사용자 생성 시작 name: {}, 프로필 이미지: {} ",
        userCreateRequest.username(),
        (optionalProfileCreateRequest.isEmpty() ? "프로필 없음" : "프로필 있음")
    );
    log.debug("[UserService] 사용자 생성 요청 정보 name: {}, email: {}, password: {}, 프로필 이미지: {}",
        username,
        PrivacyMaskingUtil.maskEmail(email),
        PrivacyMaskingUtil.maskPassword(userCreateRequest.password()),
        (optionalProfileCreateRequest.isEmpty() ? "프로필 없음" : "프로필 있음")
    );
    if (optionalProfileCreateRequest.isPresent()) {
      BinaryContentCreateRequest profileRequest = optionalProfileCreateRequest.get();
      log.debug("[UserService] 프로필 요청 정보: 파일 fileName: {}, type: {}, size: {} byte",
          profileRequest.fileName(),
          profileRequest.contentType(),
          profileRequest.bytes().length);
    }

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
    String password = userCreateRequest.password();

    User user = new User(username, email, password, nullableProfile);
    Instant now = Instant.now();
    log.info("[UserService] userStatus 생성 시작: name: {}", user.getUsername());
    UserStatus userStatus = new UserStatus(user, now);
    log.info("[UserService] userStatus 생성 성공 userName: {},userStatusId: {}", user.getUsername(),
        userStatus.getId());
    userRepository.save(user);
    log.info("[UserService] 사용자 생성 성공 userName: {},userId: {}", user.getUsername(), user.getId());
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    log.info("[UserService] 사용자 수정 시작 targetUser: {}", userId);
    log.debug(
        "[UserService] 사용자 수정 요청 정보 newName: {}, newEmail: {}, newPassword: {}, new 프로필 이미지: {}",
        userUpdateRequest.newUsername(),
        PrivacyMaskingUtil.maskEmail(userUpdateRequest.newEmail()),
        PrivacyMaskingUtil.maskPassword(userUpdateRequest.newPassword()),
        (optionalProfileCreateRequest.isEmpty() ? "프로필 없음" : "프로필 있음")
    );
    if (optionalProfileCreateRequest.isPresent()) {
      BinaryContentCreateRequest profileRequest = optionalProfileCreateRequest.get();
      log.debug("[UserService] 수정 프로필 요청 정보: 파일 fileName: {}, type: {}, size: {} byte",
          profileRequest.fileName(),
          profileRequest.contentType(),
          profileRequest.bytes().length);
    }
    User user = userRepository.findById(userId)
        .orElseThrow(() ->
            new NoSuchElementException("User with id " + userId + " not found"));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);
    log.info("[UserService] 사용자 수정 성공 targetUser: {}", userId);
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.info("[UserService] 사용자 삭제 시작 id: {}", userId);
    if (userRepository.existsById(userId)) {
      throw new NoSuchElementException("User with id " + userId + " not found");
    }
    userRepository.deleteById(userId);
    log.info("[UserService] 사용자 삭제 성공 id: {}", userId);
  }
}
