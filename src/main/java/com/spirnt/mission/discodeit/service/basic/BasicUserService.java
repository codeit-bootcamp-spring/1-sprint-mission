package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.spirnt.mission.discodeit.dto.user.UserCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.dto.user.UserUpdateRequest;
import com.spirnt.mission.discodeit.enity.BinaryContent;
import com.spirnt.mission.discodeit.enity.User;
import com.spirnt.mission.discodeit.enity.UserStatus;
import com.spirnt.mission.discodeit.enity.UserStatusType;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.BinaryContentRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.repository.UserStatusRepository;
import com.spirnt.mission.discodeit.service.BinaryContentService;
import com.spirnt.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserMapper userMapper;

  private final UserRepository userRepository;

  private final BinaryContentService binaryContentService;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {
    String email = userCreateRequest.email();
    String username = userCreateRequest.username();
    String password = userCreateRequest.password();
    // 파라미터 검증
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException(
          "User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException(
          "User with name " + username + " already exists");
    }
    // User 생성, 저장
    User user = userRepository.save(new User(username, email, password));
    // 관련 도메인
    UserStatus userStatus = userStatusRepository.save(
        new UserStatus(user, UserStatusType.ONLINE, Instant.now()));
    // 프로필 이미지 저장
    BinaryContent binaryContent = null;
    if (binaryContentCreateRequest != null) {
      BinaryContentDto binaryContentDto = binaryContentService.create(binaryContentCreateRequest);
      binaryContent = binaryContentRepository.findById(binaryContentDto.getId())
          .orElseThrow(() -> new RuntimeException());
    }
    user.setProfileAndStatus(binaryContent, userStatus);
    return userMapper.toDto(user);
  }

  @Override
  public UserDto find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id" + userId + " not found"));
    return userMapper.toDto(user);
  }

  @Transactional(readOnly = true)
  @Override
  public List<UserDto> findAll() {
    List<User> users = userRepository.findAllFetchJoin();
    return users.stream()
        .sorted(Comparator.comparing(user -> user.getCreatedAt()))
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {
    String email = userUpdateRequest.newEmail();
    String username = userUpdateRequest.newUsername();
    String password = userUpdateRequest.newPassword();
    // 영속성 컨텍스트에서 관리하게 됨
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id: " + userId + " not found"));
    // 파라미터 검증
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException(
          "User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException(
          "User with name " + username + " already exists");
    }
    // 프로필 이미지 저장
    // 기존 프로필은 cascade로 자동 삭제
    BinaryContentDto binaryContentDto =
        (binaryContentCreateRequest != null) ? binaryContentService.create(
            binaryContentCreateRequest) : null;
    BinaryContent binaryContent =
        (binaryContentDto == null) ? null
            : binaryContentRepository.findById(binaryContentDto.getId())
                .orElse(null);
    user.update(username, email, password, binaryContent);
    return userMapper.toDto(user);
  }

  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id" + userId + " not found"));
    userRepository.delete(user);
  }

}
