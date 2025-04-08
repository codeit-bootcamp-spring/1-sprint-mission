package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @InjectMocks
  private BasicUserService basicUserService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Mock
  private UserMapper userMapper;

  private UserCreateRequest userCreateRequest;
  private UserUpdateRequest userUpdateRequest;
  private User user;
  private User newUser;
  private UserDto userDto;
  private UserDto newUserDto;

  @BeforeEach
  void setUp() {
    userCreateRequest = new UserCreateRequest("testuser", "test@example.com", "qwer1234!");
    user = new User(userCreateRequest.username(), userCreateRequest.email(),
        userCreateRequest.password(), null);
    userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, null);
    userUpdateRequest = new UserUpdateRequest("newUser", "new@example.com", "qwer12345!");
    newUser = new User(userCreateRequest.username(), userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(), null);
    newUserDto = new UserDto(newUser.getId(), newUser.getUsername(), newUser.getEmail(), null,
        null);
  }

  @Test
  void create_Success() {
    // given
    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(userRepository.existsByUsername(anyString())).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    // when
    UserDto result = basicUserService.create(userCreateRequest, Optional.empty());

    // then
    assertNotNull(result);
    assertEquals(result.username(), user.getUsername());
    then(userRepository).should().save(any(User.class));
  }

  @Test
  void create_Fail_DuplicateEmail() {
    // given
    given(userRepository.existsByEmail(anyString())).willReturn(true);

    // when, then
    assertThrows(DuplicateEmailException.class,
        () -> basicUserService.create(userCreateRequest, Optional.empty()));
  }

  @Test
  void create_Fail_DuplicateUsername() {
    // given
    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(userRepository.existsByUsername(anyString())).willReturn(true);

    // when, then
    assertThrows(DuplicateUsernameException.class,
        () -> basicUserService.create(userCreateRequest, Optional.empty()));
  }

  @Test
  void update_Success() {
    // given
    given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
    given(userRepository.existsByEmail(anyString())).willReturn(false);
    given(userRepository.existsByUsername(anyString())).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(newUser);
    given(userMapper.toDto(any(User.class))).willReturn(newUserDto);

    // when
    UserDto result = basicUserService.update(user.getId(), userUpdateRequest, Optional.empty());

    // then
    assertNotNull(result);
    assertEquals(result.username(), newUser.getUsername());
    then(userRepository).should().save(any(User.class));
  }

  @Test
  void update_Fail_NotFoundUser() {
    // given
    given(userRepository.findById(user.getId())).willReturn(Optional.empty());

    // when, then
    assertThrows(UserNotFoundException.class,
        () -> basicUserService.update(user.getId(), userUpdateRequest, Optional.empty()));
  }

  @Test
  void delete_Success() {
    // given
    given(userRepository.existsById(user.getId())).willReturn(true);

    // when
    basicUserService.delete(user.getId());

    // then
    then(userRepository).should().deleteById(user.getId());
  }

  @Test
  void delete_Fail_NotFoundUser() {
    // given
    given(userRepository.existsById(user.getId())).willReturn(false);

    // when, then
    assertThrows(UserNotFoundException.class, () -> basicUserService.delete(user.getId()));
  }
}