package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserValidator validator;
  private final UserMapper userMapper;

  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public UserDto create(UserCreateRequest userRequest,
      Optional<BinaryContentRequest> binaryContentRequest) {
    validator.validate(userRequest.username(), userRequest.email());
    validateDuplicateName(userRequest.username());
    validateDuplicateEmail(userRequest.email());

    BinaryContent profile = binaryContentRequest
        .map(binaryContentService::create)
        .flatMap(dto -> binaryContentRepository.findById(dto.id()))
        .orElse(null);

    return userMapper.toDto(
        userRepository.save(
            new User(userRequest.username(), userRequest.email(), userRequest.password(), profile))
    );
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));

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
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentRequest> binaryContentRequest) {
    validator.checkEmailFormat(userUpdateRequest.newEmail());
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다."));
    validateDuplicateName(userUpdateRequest.newUsername());
    validateDuplicateEmail(userUpdateRequest.newEmail());

    BinaryContent profile = binaryContentRequest
        .map(binaryContentService::create)
        .flatMap(dto -> binaryContentRepository.findById(dto.id()))
        .orElse(null);
    user.update(profile, userUpdateRequest.newUsername(), userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword());

    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("[ERROR] 존재하지 않는 유저입니다.");
    }
    userRepository.deleteById(userId);
  }

  @Override
  public void validateDuplicateName(String name) {
    userRepository.findAll().forEach(user -> user.validateDuplicateName(name));
  }

  @Override
  public void validateDuplicateEmail(String email) {
    userRepository.findAll().forEach(user -> user.validateDuplicateEmail(email));
  }
}
