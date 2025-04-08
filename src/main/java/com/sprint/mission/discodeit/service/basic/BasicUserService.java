package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest userRequest, MultipartFile file) {
    validateDuplicateName(userRequest.username());
    validateDuplicateEmail(userRequest.email());

    BinaryContent profile = Optional.ofNullable(file)
        .flatMap(binaryContentService::resolveProfileRequest)
        .map(binaryContentService::create)
        .flatMap(dto -> binaryContentRepository.findById(dto.id()))
        .orElse(null);

    User user = new User(userRequest.username(), userRequest.email(), userRequest.password(), profile);
    User savedUser = userRepository.save(user);
    log.info("User entity saved: id = {}", savedUser.getId());

    return userMapper.toDto(savedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(
            ErrorCode.USER_NOT_FOUND,
            Map.of("userId", userId)
        ));

    return userMapper.toDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAll();

    return users.stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile file) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(
            ErrorCode.USER_NOT_FOUND,
            Map.of("userId", userId)
        ));

    BinaryContent profile = Optional.ofNullable(file)
        .flatMap(binaryContentService::resolveProfileRequest)
        .map(binaryContentService::create)
        .flatMap(dto -> binaryContentRepository.findById(dto.id()))
        .orElse(null);

    if (userUpdateRequest.newUsername() != null) {
      validateDuplicateName(userUpdateRequest.newUsername());
      user.updateName(userUpdateRequest.newUsername());
      log.info("User entity updated - username changed: id = {}", user.getId());
    }
    if (userUpdateRequest.newEmail() != null) {
      validateDuplicateEmail(userUpdateRequest.newEmail());
      user.updateEmail(userUpdateRequest.newEmail());
      log.info("User entity updated - email changed: id = {}", user.getId());
    }
    if (userUpdateRequest.newPassword() != null && !userUpdateRequest.newPassword().isBlank()) {
      user.updatePassword(userUpdateRequest.newPassword());
      log.info("User entity updated - password changed: id = {}", user.getId());
    }
    if (profile != null) {
      user.updateProfile(profile);
      log.info("User entity updated - profile changed: profileId = {}, with userId = {}",
          profile.getId(), user.getId());
    }

    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(
          ErrorCode.USER_NOT_FOUND,
          Map.of("userId", userId)
      );
    }
    userRepository.deleteById(userId);

    log.info("User entity deleted: id = {}", userId);
  }

  @Override
  public void validateDuplicateName(String username) {
    if (userRepository.existsByUsername(username)) {
      throw new DuplicateUsernameException(
          ErrorCode.DUPLICATE_USERNAME,
          Map.of("username", username)
      );
    }
    log.debug("Username validation passed for {}", username);
  }

  @Override
  public void validateDuplicateEmail(String email) {
    if (userRepository.existsByEmail(email)) {
      throw new DuplicateEmailException(
          ErrorCode.DUPLICATE_EMAIL,
          Map.of("email", email)
      );
    }
    log.debug("Email validation passed for {}", email);
  }
}
