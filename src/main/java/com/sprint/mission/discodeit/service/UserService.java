package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.binarycontent.file.FileCreateException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserEmailDuplicateException;
import com.sprint.mission.discodeit.exception.user.UserNameDuplicateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Transactional
  public UserDto createUser(UserCreateRequest userCreateRequest, MultipartFile profile) {
    log.debug("createUser() 호출");
    duplicationCheck(userCreateRequest.username(), userCreateRequest.email());

    String encodedPassword = passwordEncoder.encode(userCreateRequest.password());
    User newUser = User.create(userCreateRequest.username(), userCreateRequest.email(),
        encodedPassword);
    BinaryContent content = createProfile(profile);

    newUser = userRepository.save(newUser);
    newUser.updateProfile(content);
    log.info("User 생성. id: {}", newUser.getId());

    return userMapper.toDto(newUser);
  }

  public List<UserDto> readAll() {
    log.debug("readAll() 호출");
    Set<UUID> onlineUserIds = jwtService.getActiveJwtSessions().stream()
        .map(JwtSession::getUserId)
        .collect(Collectors.toSet());

    return userRepository.findAllWithProfile().stream()
        .map(user -> userMapper.toDto(user, onlineUserIds.contains(user.getId())))
        .toList();
  }

  @PreAuthorize("hasRole('ADMIN') or principal.user.id == #userId")
  @Transactional
  public UserDto updateUser(UUID userId, UserUpdateRequest userUpdateRequest,
      MultipartFile profile) {
    log.debug("updateUser() 호출");
    String newEmail = userUpdateRequest.newEmail();
    String newUsername = userUpdateRequest.newUsername();
    String newPassword = userUpdateRequest.newPassword();
    User user = userRepository.findByIdWithProfile(userId)
        .orElseThrow(() -> new UserNotFoundException(Map.of("id", userId)));

    if (newEmail != null) {
      if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
        throw new UserEmailDuplicateException(Map.of("newEmail", newEmail));
      }
      user.updateEmail(newEmail);
    }
    if (newUsername != null) {
      if (!user.getUsername().equals(newUsername) && userRepository.existsByUsername(newUsername)) {
        throw new UserNameDuplicateException(Map.of("newUsername", newUsername));
      }
      user.updateName(newUsername);
    }
    if (newPassword != null) {
      user.updatePassword(passwordEncoder.encode(newPassword));
    }
    if (profile != null) {
      user.getProfile().ifPresent(content -> binaryContentStorage.delete(content.getId()));
      BinaryContent content = createProfile(profile);
      user.updateProfile(content);
    }

    log.info("user 정보 수정. id: {}", user.getId());
    userRepository.save(user);
    return userMapper.toDto(user);
  }

  @PreAuthorize("hasRole('ADMIN') or principal.user.id == #userId")
  @Transactional
  public void deleteUser(UUID userId) {
    log.debug("deleteUser() 호출");
    userRepository.findByIdWithProfile(userId)
        .ifPresent(user -> {
          user.getProfile().ifPresent(content -> binaryContentStorage.delete(content.getId()));
          log.info("user 삭제. id: {}", user.getId());
          userRepository.delete(user);
        });
  }

  private void duplicationCheck(String username, String email) {
    log.debug("duplicationCheck() 호출");
    if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
      log.error("중복된 이메일 혹은 이름. email: {}, username: {}", email, username);
      throw new UserAlreadyExistException(Map.of());
    }
  }


  private BinaryContent createProfile(MultipartFile profile) {
    log.debug("createProfile() 호출");
    if (profile == null || profile.isEmpty()) {
      return null;
    }

    long size = profile.getSize();
    String fileName = profile.getOriginalFilename();
    String contentType = fileName.substring(fileName.lastIndexOf('.'));

    BinaryContent content = binaryContentRepository
        .save(BinaryContent.create(size, fileName, contentType));
    log.info("BinaryContent 생성. id: {}", content.getId());
    try {
      binaryContentStorage.put(content.getId(), profile.getBytes());
    } catch (IOException e) {
      log.error("파일 생성 실패");
      throw new FileCreateException(Map.of());
    }

    return content;
  }
}