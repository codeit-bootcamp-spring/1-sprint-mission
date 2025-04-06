package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
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

    log.info("사용자 생성 요청 - username: {}, email: {}", username, email);

    if (userRepository.existsByEmail(email)) {
      log.error("사용자 생성 실패 - 이미 존재하는 이메일: {}", email);
      throw new UserException.DuplicateEmailException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      log.error("사용자 생성 실패 - 이미 존재하는 사용자명: {}", username);
      throw new UserException.DuplicateUsernameException("User with username " + username + " already exists");
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
          log.debug("프로필 이미지 생성 - fileName: {}, contentType: {}, size: {} bytes", 
              fileName, contentType, bytes.length);
          return binaryContent;
        })
        .orElse(null);
    String password = userCreateRequest.password();

    User user = new User(username, email, password, nullableProfile);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);
    log.info("사용자 생성 완료 - userId: {}, username: {}", user.getId(), username);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    log.debug("사용자 조회 - userId: {}", userId);
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> {
          log.error("사용자 조회 실패 - 존재하지 않는 userId: {}", userId);
          return new UserException.UserNotFoundException("User with id " + userId + " not found");
        });
  }

  @Override
  public List<UserDto> findAll() {
    log.debug("전체 사용자 목록 조회");
    return userRepository.findAllWithProfileAndStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    log.info("사용자 정보 수정 요청 - userId: {}", userId);
    
    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("사용자 수정 실패 - 존재하지 않는 userId: {}", userId);
          return new UserException.UserNotFoundException("User with id " + userId + " not found");
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      log.error("사용자 수정 실패 - 이미 존재하는 이메일: {}", newEmail);
      throw new UserException.DuplicateEmailException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername)) {
      log.error("사용자 수정 실패 - 이미 존재하는 사용자명: {}", newUsername);
      throw new UserException.DuplicateUsernameException("User with username " + newUsername + " already exists");
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
          log.debug("프로필 이미지 수정 - fileName: {}, contentType: {}, size: {} bytes", 
              fileName, contentType, bytes.length);
          return binaryContent;
        })
        .orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);
    log.info("사용자 정보 수정 완료 - userId: {}, username: {}", userId, newUsername);
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.info("사용자 삭제 요청 - userId: {}", userId);
    
    if (!userRepository.existsById(userId)) {
      log.error("사용자 삭제 실패 - 존재하지 않는 userId: {}", userId);
      throw new UserException.UserNotFoundException("User with id " + userId + " not found");
    }

    userRepository.deleteById(userId);
    log.info("사용자 삭제 완료 - userId: {}", userId);
  }
}
