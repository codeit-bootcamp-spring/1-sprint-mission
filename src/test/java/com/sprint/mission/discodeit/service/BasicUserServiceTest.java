package com.sprint.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.*;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;
  @InjectMocks
  private BasicUserService basicUserService;

  private UserCreateRequestDto validRequest;

  @BeforeEach
  void setUp() {
    validRequest = new UserCreateRequestDto("홍길동", "hong@naver.com", "1234");
  }

  @Test
  void 유저_생성_성공() {
    when(userRepository.existsByEmail("hong@naver.com")).thenReturn(false);
    when(userRepository.existsByUsername("홍길동")).thenReturn(false);

    UserDto userDto = new UserDto();
    userDto.setEmail("hong@naver.com");
    userDto.setUsername("홍길동");

    when(userMapper.toDto(any(User.class))).thenReturn(userDto);

    UserDto result = basicUserService.createUser(validRequest, null);

    assertNotNull(result);
    assertEquals("홍길동", result.getUsername());
    assertEquals("hong@naver.com", result.getEmail());

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());

    User savedUser = userCaptor.getValue();
    assertEquals("hong@naver.com", savedUser.getEmail());
    assertEquals("홍길동", savedUser.getUsername());
    assertEquals("1234", savedUser.getPassword());
  }

  @Test
  void 유저_생성_실패_필수값누락() {
    assertThrows(InvalidUserInputException.class,
        () -> basicUserService.createUser(new UserCreateRequestDto(null, "test@naver.com", "1234"),
            null));
    assertThrows(InvalidUserInputException.class,
        () -> basicUserService.createUser(new UserCreateRequestDto("테스터", null, "1234"), null));
    assertThrows(InvalidUserInputException.class,
        () -> basicUserService.createUser(new UserCreateRequestDto("테스터", "test@naver.com", null),
            null));
  }

  @Test
  void 유저_생성_실패_중복이메일() {
    when(userRepository.existsByEmail("hong@naver.com")).thenReturn(true);
    assertThrows(DuplicatedEmailException.class,
        () -> basicUserService.createUser(validRequest, null));
  }

  @Test
  void 유저_생성_실패_중복이름() {
    when(userRepository.existsByUsername("홍길동")).thenReturn(true);
    assertThrows(DuplicatedUsernameException.class,
        () -> basicUserService.createUser(validRequest, null));
  }

  @Test
  void 유저_업데이트_성공() {
    UUID userId = UUID.randomUUID();
    User existingUser = new User("oldName", "old@email.com", "oldPassword", null);
    existingUser.setStatus(new UserStatus(existingUser, Instant.now()));

    UserUpdateRequestDto request = new UserUpdateRequestDto("newName", "new@email.com",
        "newPassword");
    UserDto expectedDto = new UserDto();
    expectedDto.setUsername("newName");
    expectedDto.setEmail("new@email.com");

    given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
    given(userMapper.toDto(any(User.class))).willReturn(expectedDto);

    UserDto result = basicUserService.updateUser(userId, request, null);

    assertNotNull(result);
    assertEquals("newName", result.getUsername());
    assertEquals("new@email.com", result.getEmail());
    then(userRepository).should().findById(userId);
    then(userMapper).should().toDto(existingUser);
  }

  @Test
  void 유저_업데이트_실패_없는유저() {
    UUID userId = UUID.randomUUID();
    UserUpdateRequestDto request = new UserUpdateRequestDto("newName", "new@email.com",
        "newPassword");

    given(userRepository.findById(userId)).willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class,
        () -> basicUserService.updateUser(userId, request, null));
  }

  @Test
  void 유저_삭제_성공() {
    UUID userId = UUID.randomUUID();
    User user = new User("홍길동", "hong@naver.com", "1234", null);
    user.setStatus(new UserStatus(user, Instant.now()));

    given(userRepository.findById(userId)).willReturn(Optional.of(user));

    basicUserService.deleteUser(userId);

    then(userRepository).should().findById(userId);
    then(userRepository).should().delete(user);
  }

  @Test
  void 유저_삭제_실패_없는유저() {
    UUID userId = UUID.randomUUID();
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> basicUserService.deleteUser(userId));
  }
}
