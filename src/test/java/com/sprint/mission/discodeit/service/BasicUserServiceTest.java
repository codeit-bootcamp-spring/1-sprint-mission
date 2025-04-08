package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.UserEmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private UserStatusRepository userStatusRepository;

  @InjectMocks
  private BasicUserService userService;

  @Test
  @DisplayName("User 생성 - 성공")
  void createUserSuccess() {
    UserCreateRequest userCreateRequest = new UserCreateRequest("yubin", "yubin@codeit.com",
        "1234");
    User user = new User(userCreateRequest.username(), userCreateRequest.email(),
        userCreateRequest.password(), null);
    UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(),
        null, true);
    UserStatus userStatus = new UserStatus(user, Instant.now());

    given(userRepository.existsByUsername(user.getUsername())).willReturn(false);
    given(userRepository.existsByEmail(user.getEmail())).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userStatusRepository.save(any(UserStatus.class))).willReturn(userStatus);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    UserDto result = userService.create(userCreateRequest, Optional.empty());
    assertThat(result.username()).isEqualTo("yubin");
  }

  @Test
  @DisplayName("User 생성 - 실패 (중복된 유저 이름)")
  void createUserFail() {
    UserCreateRequest userCreateRequest = new UserCreateRequest("minji", "minji@gmail.com",
        "1234");
    User user = new User(userCreateRequest.username(), userCreateRequest.email(),
        userCreateRequest.password(), null);

    given(userRepository.existsByUsername(user.getUsername())).willReturn(true);
    assertThatThrownBy(() -> userService.create(userCreateRequest, Optional.empty()))
        .isInstanceOf(UsernameAlreadyExistsException.class)
        .hasMessageContaining("중복된 유저 이름입니다.");
  }

  @Test
  @DisplayName("User 업데이트 - 성공")
  void updateUserSuccess() {
    UUID userId = UUID.randomUUID();
    User existingUser = new User("yubin", "yubin@codeit.com", "1234", null);

    UserUpdateRequest userUpdateRequest = new UserUpdateRequest("Yubin", null, null);
    UserDto expectedDto = new UserDto(userId, userUpdateRequest.newUsername(),
        userUpdateRequest.newEmail(), null,
        true);

    given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
    given(userRepository.existsByUsername(userUpdateRequest.newUsername())).willReturn(false);
    given(userMapper.toDto(existingUser)).willReturn(expectedDto);

    UserDto result = userService.update(userId, userUpdateRequest, Optional.empty());

    assertThat(result.username()).isEqualTo(userUpdateRequest.newUsername());
  }

  @Test
  @DisplayName("User 수정 - 실패 (중복된 이메일)")
  void updateUserFail() {
    UUID userId = UUID.randomUUID();
    User existingUser = new User(null, "yubin@codeit.com", null, null);

    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(null, "yubin@codeit.com",
        null);

    given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
    given(userRepository.existsByEmail(userUpdateRequest.newEmail())).willReturn(true);

    assertThatThrownBy(() -> userService.update(userId, userUpdateRequest, Optional.empty()))
        .isInstanceOf(UserEmailAlreadyExistsException.class)
        .hasMessageContaining("중복된 이메일입니다.");
  }

  @Test
  @DisplayName("User 삭제 - 성공")
  void deleteUserSuccess() {
    UUID userId = UUID.randomUUID();

    given(userRepository.existsById(userId)).willReturn(true);
    willDoNothing().given(userRepository).deleteById(userId);

    userService.delete(userId);

    then(userRepository).should().deleteById(userId);
  }

  @Test
  @DisplayName("User 삭제 - 실패 (존재하지 않는 유저)")
  void deleteUserFail_UserNotFound() {
    UUID userId = UUID.randomUUID();

    given(userRepository.existsById(userId)).willReturn(false);

    assertThatThrownBy(() -> userService.delete(userId))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessageContaining("유저를 찾을 수 없습니다");

    then(userRepository).should(never()).deleteById(any());
  }
}
