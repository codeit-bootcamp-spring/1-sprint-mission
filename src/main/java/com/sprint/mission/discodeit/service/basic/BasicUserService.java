package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor  // 사용 시 필수 필드에 private final 필수
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDto create(UserCreateRequest userRequest,
      Optional<BinaryContentCreateRequest> profileRequest) {

    log.debug("사용자 생성 시작: {}", userRequest);

    String username = userRequest.username();
    String email = userRequest.email();
    String password = passwordEncoder.encode(userRequest.password()); // 비밀번호 암호화

    if (userRepository.existsByEmail(email)) {
      throw UserAlreadyExistException.withEmail(email);
    }
    if (userRepository.existsByUsername(username)) {
      throw UserAlreadyExistException.withUsername(username);
    }

    BinaryContent profile = convertToBinaryContent(profileRequest);

    User user = new User(username, email, password, profile);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);

    log.info("사용자 생성 완료: id={}, username={}", user.getId(), username);

    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID userId) {

    log.debug("사용자 조회 시작: id={}", userId);

    UserDto userDto = userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    log.info("사용자 조회 완료: id={}", userId);

    return userDto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {

    log.debug("모든 사용자 조회 시작");

    List<UserDto> userDtos = userRepository.findAllWithProfileAndStatus().stream()
        .map(userMapper::toDto)
        .toList();

    log.info("모든 사용자 조회 완료: count={}", userDtos.size());

    return userDtos;
  }

  @Override
  public UserDto update(UUID userId, UserUpdateRequest userRequest,
      Optional<BinaryContentCreateRequest> profileRequest) {

    log.debug("사용자 수정 시작: id={}, request={}", userId, userRequest);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));

    String newUsername = userRequest.newUsername();
    String newEmail = userRequest.newEmail();
    String newPassword = userRequest.newPassword();

    if (userRepository.existsByEmail(newEmail)) {
      throw UserAlreadyExistException.withEmail(newEmail);
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw UserAlreadyExistException.withUsername(newUsername);
    }

    BinaryContent profile = convertToBinaryContent(profileRequest);

    user.update(newUsername, newEmail, newPassword, profile);

    log.info("사용자 수정 완료: id={}", userId);

    return userMapper.toDto(user);
  }

  @Override
  public void delete(UUID userId) {

    log.debug("사용자 삭제 시작: id={}", userId);

    if (!userRepository.existsById(userId)) {
      throw UserNotFoundException.withId(userId);
    }

    userRepository.deleteById(userId);

    log.info("사용자 삭제 완료: id={}", userId);
  }

  @Override
  public UserDto updateRole(RoleUpdateRequest request) {

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withId(request.userId()));

    user.updateRole(request.newRole());

    return userMapper.toDto(user);
  }

  // Optional<CreateBinaryContentRequest> -> BinaryContent
  private BinaryContent convertToBinaryContent(
      Optional<BinaryContentCreateRequest> binaryContentRequest) {
    return binaryContentRequest
        .map(request -> {
          String fileName = request.fileName();
          String contentType = request.contentType();
          byte[] bytes = request.bytes();
          BinaryContent binaryContent = new BinaryContent(
              fileName,
              (long) bytes.length,
              contentType
          );
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
  }
}