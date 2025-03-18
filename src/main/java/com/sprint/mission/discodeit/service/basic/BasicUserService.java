package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
//
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
//
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;
  //
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;
  //
  private final InputHandler inputHandler;

  @Transactional
  @Override
  public UserDto createUser(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {

    // username과 email이 다른 유저와 같이 겹치는지 검증
    if (userRepository.existsByUsername(userCreateRequest.username())) {
      throw new IllegalArgumentException("동일한 username이 존재합니다."); // 전역 에러에서 400 처리
    }
    if (userRepository.existsByEmail(userCreateRequest.email())) {
      throw new IllegalArgumentException("동일한 email이 존재합니다."); // 전역 에러에서 400 처리
    }

    // binaryContentService를 통해 binaryContent 도메인 객체 생성
    if (binaryContentCreateRequest != null) {
      binaryContentService.createBinaryContent(binaryContentCreateRequest);
    }

    // user 도메인 객체 생성
    User user = User.builder()
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .password(userCreateRequest.password())
        .build();
    userRepository.save(user);

    // UserStatus 도메인 객체 생성
    UserStatusCreateRequest userStatusCreateRequest =
        new UserStatusCreateRequest(
            user,
            Instant.now());
    userStatusService.createUserStatus(userStatusCreateRequest);

    return userMapper.toDto(user);
  }

  @Override
  public List<UserDto> showAllUsers() {
    // TODO 예외 처리
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public UserDto getUserById(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("유저(" + id + ")가 없습니다."));
    return userMapper.toDto(user);
  }


  @Transactional
  @Override
  public UserDto updateUserInfo(UUID id, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {

    // boolean isUpdated = false; JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("유저(" + id + ")를 찾지 못했습니다."));

    if (userUpdateRequest.newUsername() != null) {
      if (userRepository.existsByUsername(userUpdateRequest.newUsername())) {
        throw new IllegalArgumentException("같은 username가 존재합니다.");
      }
      user.updateUsername(userUpdateRequest.newUsername());
    }
    if (userUpdateRequest.newEmail() != null) {
      if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
        throw new IllegalArgumentException("같은 email이 존재합니다.");
      }
      user.updateEmail(userUpdateRequest.newEmail());
    }
    if (userUpdateRequest.newPassword() != null) {
      user.updatePassword(userUpdateRequest.newPassword());
    }

    if (binaryContentCreateRequest != null) {
      user.updateProfile(binaryContentMapper.toEntity(
          binaryContentService.createBinaryContent(binaryContentCreateRequest))
      );
    }

    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void removeUserById(UUID id) {
    String keyword = inputHandler.getYesNOInput();
    if (keyword.equalsIgnoreCase("y")) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new NoSuchElementException("유저(" + id + ")가 없습니다."));
      userStatusService.delteUserStatusByUserId(user.getId());
      binaryContentService.deleteBinaryContentById(user.getProfile().getId());

      userRepository.deleteById(id);
    }
  }
}
