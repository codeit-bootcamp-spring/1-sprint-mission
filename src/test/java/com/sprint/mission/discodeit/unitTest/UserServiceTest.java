package com.sprint.mission.discodeit.unitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService userService;

  private UUID userId;
  private User user;
  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    user = new User("woody", "woody@naver.com", "1234", null);
    userDto = new UserDto(userId, "woody", "woody@naver.com", null, true);
  }

  @Test
  void shouldCreateUserSuccess() { //유저 생성 성공
    //given
    UserCreateRequest request = new UserCreateRequest("woody", "woody@naver.com", "1234");
    given(userRepository.existsByUsername(request.username())).willReturn(false);
    given(userRepository.existsByEmail(request.email())).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    //when
    UserDto createdUser = userService.create(request, Optional.empty());

    //then
    assertThat(createdUser).isNotNull();
    assertThat(createdUser.username()).isEqualTo(request.username());
  }

  @Test
  void shouldThrowExceptionWHenCreateUserWithDuplicateEmail() { //이메일 중복 체크 test
    //given
    UserCreateRequest request = new UserCreateRequest("woody", "woody@naver.com", "1234");
    given(userRepository.existsByEmail(request.email())).willReturn(true);

    //when & then
    assertThatThrownBy(() -> userService.create(request, Optional.empty()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("User with email " + request.email() + " already exists");
  }

  @Test
  void shouldFindUserSuccess() { //유저 조회
    //given
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userMapper.toDto(user)).willReturn(userDto);

    //when
    UserDto findUser = userService.find(userId);

    //then
    assertThat(findUser).isNotNull();
    assertThat(findUser.username()).isEqualTo(user.getUsername());
  }

  @Test
  void shouldThrowExceptionWhenUserNotFound() { //유저 조회 실패
    //given
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    //when & then
    assertThatThrownBy(() -> userService.find(userId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("User with id " + userId + " not found");
  }

  @Test
  void shouldDeleteUserSuccess() {
    //given
    given(userRepository.existsById(userId)).willReturn(true);

    //when
    userService.delete(userId);

    //then
    verify(userRepository, times(1)).deleteById(userId);
    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  void shouldThrowExceptionWhenDeleteNonExistingUser() { //존재하지 않는 유저 삭제
    //given
    given(userRepository.existsById(userId)).willReturn(false);

    //when & then
    assertThatThrownBy(() -> userService.delete(userId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("User with id " + userId + " not found");

    //existsById 검증 코드
    verify(userRepository, times(1)).existsById(userId);
  }

}