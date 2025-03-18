package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequst;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service // 서비스 Bean
@RequiredArgsConstructor // 생성자
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentService binaryContentService;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequst userCreateRequst,
      Optional<BinaryContentCreateRequest> profileCreateRequest) {
    //username,email 중복 확인
    validateUsernameAndEmail(userCreateRequst.username(), userCreateRequst.email());

    //프로필 이미지 Id 체크
    UUID profileId = profileIdCheck(profileCreateRequest);
    BinaryContent profile = binaryContentRepository.findById(profileId)
        .orElseThrow(
            () -> new NoSuchElementException("Profile with id " + profileId + " not found"));

    //User 생성
    User user = new User(
        userCreateRequst.username(),
        userCreateRequst.email(),
        userCreateRequst.password(),
        profile
    );
    User createdUser = userRepository.save(user);

    //UserStatus 생성
    userStatusRepository.save(createdUser.getStatus());

    return userMapper.toDto(createdUser);
  }


  @Override
  public UserDto findById(UUID userId) {
    return userRepository.findById(userId)
        .map(user -> userMapper.toDto(user))
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(user -> userMapper.toDto(user))
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    validateUsernameAndEmail(userUpdateRequest.newUsername(), userUpdateRequest.newEmail());
    UUID profileId = profileIdCheck(profileCreateRequest);
    BinaryContent profile = binaryContentRepository.findById(profileId)
        .orElseThrow(
            () -> new NoSuchElementException("Profile with id " + profileId + " not found"));

    user.update(
        userUpdateRequest.newUsername(),
        userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(),
        profile
    );
    User savedUser = userRepository.save(user);
    return userMapper.toDto(savedUser);
  }

  @Transactional
  @Override
  public void deleteById(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    //관련된 프로필 이미지 삭제
    if (user.getProfile().getId() != null) {
      binaryContentRepository.deleteById(user.getProfile().getId());
    }
    //UserStatus 삭제
    userStatusRepository.deleteByUserId(user.getId());
    //유저 삭제
    userRepository.deleteById(user.getId());
  }

  //프로필 이미지 Id 체크
  public UUID profileIdCheck(Optional<BinaryContentCreateRequest> profileCreateRequest) {
    UUID profileId = profileCreateRequest.map(p -> {
      binaryContentStorage.put(UUID.randomUUID(), p.bytes());
      BinaryContent binaryContent = new BinaryContent(p.fileName(), (long) p.bytes().length,
          p.contentType());
      return binaryContentRepository.save(binaryContent).getId();
    }).orElse(null);
    return profileId;
  }

  //username, email 다른 유저와 같은지 체크
  public void validateUsernameAndEmail(String username, String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }
  }


}
