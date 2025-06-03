package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserEmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private UserStatusRepository userStatusRepository;

  @InjectMocks
  private BasicUserService userService;

  private UUID userId;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    user = new User("testUser", "test@example.com", "password", null);
    user.setId(userId);
    userDto = new UserDto(userId, "testUser", "test@example.com", null, true);
  }

  @Test
  void createUserShouldSucceed() {
    // given
    UserCreateRequest request = new UserCreateRequest("testUser", "test@example.com", "password");

    given(userRepository.existsByEmail(request.email())).willReturn(false);
    given(userRepository.existsByUsername(request.username())).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    // when
    UserDto result = userService.create(request, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo("testUser");
    then(userRepository).should().save(any(User.class));
  }

  @Test
  void createUserShouldFail() {
    // given
    UserCreateRequest request = new UserCreateRequest("testUser", "test@example.com", "password");

    given(userRepository.existsByEmail(request.email())).willReturn(true);

    // when & then
    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(UserEmailAlreadyExistsException.class)
        .hasMessageContaining("이미 존재하는 이메일입니다.");

    then(userRepository).should(never()).save(any(User.class));
  }

  @Test
  void updateUserShouldSucceed() {
    // given
    UserUpdateRequest request = new UserUpdateRequest("newUser", "new@example.com", "newPassword");

    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    // when
    UserDto result = userService.update(userId, request, Optional.empty());

    // then
    assertThat(result.username()).isEqualTo("testUser"); // 기존 username 유지
    then(userRepository).should().findById(userId);
  }

  @Test
  void deleteUserShouldSucceed() {
    // given
    given(userRepository.existsById(userId)).willReturn(true);

    // when
    userService.delete(userId);

    // then
    then(userRepository).should().deleteById(user.getId());
  }

  @Test
  void deleteUser() {
    // given
    given(userRepository.existsById(userId)).willReturn(false);

    // when & then
    assertThatThrownBy(() -> userService.delete(userId))
        .isInstanceOf(UserNotFoundException.class);
  }
}
