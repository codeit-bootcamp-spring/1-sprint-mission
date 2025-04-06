package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest request,
      Optional<BinaryContentCreateRequest> profileRequest) {
    log.debug("create() called with - username: {}, email: {}", request.username(),
        request.email());

    if (userRepository.existsByEmail(request.email())) {
      log.warn("중복된 이메일 존재: {}", request.email());
      throw new DuplicateEmailException(request.email());
    }
    if (userRepository.existsByUsername(request.username())) {
      log.warn("중복된 사용자 이름 존재: {}", request.username());
      throw new DuplicateUsernameException(request.username());
    }
    BinaryContent profile = profileRequest
        .map(req -> {
          UUID storageId = binaryContentStorage.put(null, req.bytes());
          return new BinaryContent(
              storageId,
              req.fileName(),
              (long) req.bytes().length,
              req.contentType()
          );
        })
        .orElse(null);
    User user = new User(request.username(), request.email(), request.password(), profile);
    user = userRepository.save(user);

    UserStatus userStatus = new UserStatus(user, Instant.now());
    userStatusRepository.save(userStatus);

    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    log.debug("find() called with - userId: {}", userId);

    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> {
          log.error("사용자 조회 실패 - 존재하지 않는 ID: {}", userId);
          return new UserNotFoundException(userId);
        });
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest request,
      Optional<BinaryContentCreateRequest> profileRequest) {
    log.debug("update() called with - ID: {}, newUsername: {}, newEmail: {}", userId,
        request.newUsername(), request.newEmail());

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("사용자 수정 실패 - 존재하지 않는 ID: {}", userId);
          return new UserNotFoundException(userId);
        });

    if (userRepository.existsByEmail(request.newEmail()) && !user.getEmail()
        .equals(request.newEmail())) {
      log.warn("수정 중 중복 이메일 감지: {}", request.newEmail());
      throw new DuplicateEmailException(request.newEmail());
    }
    if (userRepository.existsByUsername(request.newUsername()) && !user.getUsername()
        .equals(request.newUsername())) {
      log.warn("수정 중 중복 사용자명 감지: {}", request.newUsername());
      throw new DuplicateUsernameException(request.newUsername());
    }

    if (profileRequest.isPresent()) {
      log.debug("프로필 이미지 업데이트 감지 - 기존 이미지 삭제 후 새로 설정");
      profileRequest.ifPresent(binaryContentCreateRequest -> {
        if (user.getProfile() != null) {
          binaryContentRepository.delete(user.getProfile());
        }
        user.setProfile(createProfile(binaryContentCreateRequest));
      });
    }

    user.update(request.newUsername(), request.newEmail(), request.newPassword(),
        user.getProfile());
    User updateUser = userRepository.save(user);
    return userMapper.toDto(updateUser);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.info("delete() called with - userId: {}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("사용자 삭제 실패 - 존재하지 않는 ID: {}", userId);
          return new UserNotFoundException(userId);
        });

    Optional.ofNullable(user.getProfile())
        .ifPresent(binaryContent -> {
          log.debug("사용자 프로필 삭제 - ID: {}", userId);
          binaryContentRepository.delete(binaryContent);
        });
    userRepository.deleteById(userId);
  }

  private BinaryContent createProfile(BinaryContentCreateRequest profileRequest) {
    UUID storageId = binaryContentStorage.put(null, profileRequest.bytes());
    return new BinaryContent(
        storageId,
        profileRequest.fileName(),
        (long) profileRequest.bytes().length,
        profileRequest.contentType()
    );
  }

}
