package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final AuthService authService;
  private final SessionRegistry sessionRegistry;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    log.debug("사용자 생성 시작: {}", userCreateRequest);

    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      throw UserAlreadyExistsException.withEmail(email);
    }
    if (userRepository.existsByUsername(username)) {
      throw UserAlreadyExistsException.withUsername(username);
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

    String password = passwordEncoder.encode(userCreateRequest.password());

    User user = new User(username, email, password, nullableProfile);

    userRepository.save(user);
    log.info("사용자 생성 완료: id={}, username={}", user.getId(), username);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    log.debug("사용자 조회 시작: id={}", userId);
    UserDto userDto = userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    boolean isOnline = isUserOnline(userDto.username());

    log.info("사용자 조회 완료: id={}", userId);
    return new UserDto(
        userDto.id(),
        userDto.username(),
        userDto.email(),
        userDto.profile(),
        isOnline,
        userDto.role()
    );
  }

  @Override
  public List<UserDto> findAll() {
    log.debug("모든 사용자 조회 시작");
    List<UserDto> userDtos = userRepository.findAll()
        .stream()
        .map(userMapper::toDto)
        .toList();
    log.info("모든 사용자 조회 완료: 총 {}명", userDtos.size());
    return userDtos;
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    log.debug("사용자 수정 시작: id={}, request={}", userId, userUpdateRequest);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          UserNotFoundException exception = UserNotFoundException.withId(userId);
          return exception;
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();

    if (userRepository.existsByEmail(newEmail)) {
      throw UserAlreadyExistsException.withEmail(newEmail);
    }

    if (userRepository.existsByUsername(newUsername)) {
      throw UserAlreadyExistsException.withUsername(newUsername);
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

    log.info("사용자 수정 완료: id={}", userId);
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public UserDto updateRole(UserRoleUpdateRequest request) {

    authService.requireRole(Role.ADMIN);

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withId(request.userId()));
    log.info("사용자 권한 수정 시작: id={}", request.userId());

    user.updateRole(request.newRole());
    forceLogoutUser(user.getUsername());
    log.info("사용자 권한 수정 완료: id={}", request.userId());
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.debug("사용자 삭제 시작: id={}", userId);

    if (!userRepository.existsById(userId)) {
      throw UserNotFoundException.withId(userId);
    }

    userRepository.deleteById(userId);
    log.info("사용자 삭제 완료: id={}", userId);
  }

  public boolean isOwnerOrAdmin(UUID userId, String currentUsername) {
    User targetUser = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    User currentUser = userRepository.findByUsername(currentUsername)
        .orElseThrow(() -> UserNotFoundException.withUsername(currentUsername));

    return targetUser.getId().equals(currentUser.getId()) ||
        currentUser.getRole().name().equals(Role.ADMIN.name());
  }

  @Override
  public boolean isUserOnline(String username) {
    return sessionRegistry.getAllPrincipals().stream()
        .anyMatch(principal -> {
          if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUsername().equals(username);
          }
          return false;
        });
  }

  private void forceLogoutUser(String username) {
    log.debug("사용자 강제 로그아웃 시작: username = {}", username);

    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof CustomUserDetails)
        .map(principal -> (CustomUserDetails) principal)
        .filter(userDetails -> userDetails.getUsername().equals(username))
        .forEach(userDetails -> {
          // 해당 사용자의 모든 세션을 가져와서 만료시킴
          sessionRegistry.getAllSessions(userDetails, false)
              .forEach(sessionInfo -> {
                log.info("세션 만료 처리: username = {}, sessionId = {}", username,
                    sessionInfo.getSessionId());
                sessionInfo.expireNow();
              });
        });

    log.info("사용자 강제 로그아웃 완료: username={}", username);
  }
}
