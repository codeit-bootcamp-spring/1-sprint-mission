package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
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

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();
    log.debug("사용자 생성 서비스 진입 - username: {}, email: {}", username, email);

    validateUser(username, email);
    BinaryContent nullableProfile = getProfile(optionalProfileCreateRequest);
    String password = userCreateRequest.password();

    User user = new User(username, email, password, nullableProfile);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);
    log.info("새로운 사용자 저장 완료 - username: {}, email: {}", username, email);

    return userMapper.toDto(user);
  }

  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseGet(() -> {
          log.warn("사용자를 찾을 수 없음 - userId: {}", userId);
          throw new NoSuchElementException("User with id " + userId + " not found");
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    validateUser(newUsername, newEmail);
    BinaryContent nullableProfile = getProfile(optionalProfileCreateRequest);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);

    log.info("사용자 정보 업데이트 완료 - userId: {}", userId);
    return userMapper.toDto(user);
  }

  private void validateUser(String userName, String email) {
    if (userRepository.existsByEmail(email)) {
      log.warn("이미 존재하는 이메일입니다 - email: {}", email);
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(userName)) {
      log.warn("이미 존재하는 이름입니다 - username: {}", userName);
      throw new IllegalArgumentException("User with username " + userName + " already exists");
    }

    log.info("사용자 유효성 검사 완료 - username: {}, email: {}", userName, email);
  }

  private BinaryContent getProfile(
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    return optionalProfileCreateRequest
        .map(profileRequest -> {
          log.debug("프로필 이미지 생성 요청 - fileName: {}, contentType: {}",
              profileRequest.fileName(), profileRequest.contentType());
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          log.info("프로필 이미지 생성 완료 - fileName: {}, contentType: {}",
              profileRequest.fileName(), profileRequest.contentType());
          return binaryContent;
        })
        .orElseGet(() -> {
          log.debug("프로필 이미지 없음");
          return null;
        });
  }

  @Transactional
  public void delete(UUID userId) {
    if (userRepository.existsById(userId)) {
      log.warn("사용자를 찾을 수 없음 - userId: {}", userId);
      throw new NoSuchElementException("User with id " + userId + " not found");
    }
    userRepository.deleteById(userId);
    log.info("사용자 삭제 완료 - userId: {}", userId);
  }
}
