package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService userService;

  private UUID userId;
  private User user;
  private UserDto userDto;
  private UserCreateRequest createRequest;
  private UserUpdateRequest updateRequest;
  private BinaryContent profile;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    profile = new BinaryContent("testFile", 10L, "image/jpeg");
    user = new User("testUser", "test@example.com", "test1234", profile);
    userDto = new UserDto(userId, "testUser", "test@example.com",
        new BinaryContentDto(UUID.randomUUID(), "image/jpeg", 10L, null), true);
    createRequest = new UserCreateRequest("testUser", "test@example.com", "password123");
    updateRequest = new UserUpdateRequest("newTestUser", "newTest@example.com", "newPassword123");
  }

  // create 메서드 테스트
  @Test
  @DisplayName("사용자 생성 성공 - 프로필 없이")
  void create_WithoutProfile_ShouldReturnUserDto() {
    // Given
    given(userRepository.existsByEmail(createRequest.email())).willReturn(false);
    given(userRepository.existsByUsername(createRequest.username())).willReturn(false);
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    // When
    UserDto result = userService.create(createRequest, Optional.empty());

    // Then
    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo(createRequest.username());
    then(userRepository).should().save(any(User.class));
  }

  @Test
  @DisplayName("사용자 생성 실패 - 중복된 이메일")
  void create_WithDuplicateEmail_ShouldThrowException() {
    // Given
    given(userRepository.existsByEmail(createRequest.email())).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> userService.create(createRequest, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessage("이미 존재하는 사용자입니다.");
  }

  @Test
  @DisplayName("사용자 생성 실패 - 중복된 사용자 이름")
  void create_WithDuplicateUsername_ShouldThrowException() {
    // Given
    given(userRepository.existsByEmail(createRequest.email())).willReturn(false);
    given(userRepository.existsByUsername(createRequest.username())).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> userService.create(createRequest, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessage("이미 존재하는 사용자입니다.");
  }

  // update 메서드 테스트
  @Test
  @DisplayName("사용자 업데이트 성공 - 프로필 없이")
  void update_WithoutProfile_ShouldReturnUpdatedUserDto() {
    // Given
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByEmail(updateRequest.newEmail())).willReturn(false);
    given(userRepository.existsByUsername(updateRequest.newUsername())).willReturn(false);
    given(userMapper.toDto(any(User.class))).willReturn(userDto);

    // When
    UserDto result = userService.update(userId, updateRequest, Optional.empty());

    // Then
    assertThat(result).isNotNull();
    assertThat(result.username()).isEqualTo(userDto.username());
    then(userRepository).should().findById(userId);
    // save는 호출되지 않으므로 검증에서 제외
  }

  @Test
  @DisplayName("사용자 업데이트 실패 - 존재하지 않는 사용자")
  void update_WithNonExistentUser_ShouldThrowException() {
    // Given
    given(userRepository.findById(userId)).willReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> userService.update(userId, updateRequest, Optional.empty()))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage("사용자를 찾을 수 없습니다.");
  }

  @Test
  @DisplayName("사용자 업데이트 실패 - 중복된 이메일")
  void update_WithDuplicateEmail_ShouldThrowException() {
    // Given
    given(userRepository.findById(userId)).willReturn(Optional.of(user));
    given(userRepository.existsByEmail(updateRequest.newEmail())).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> userService.update(userId, updateRequest, Optional.empty()))
        .isInstanceOf(UserAlreadyExistsException.class)
        .hasMessage("이미 존재하는 사용자입니다.");
  }

  // delete 메서드 테스트
  @Test
  @DisplayName("사용자 삭제 성공 - 존재하지 않는 경우")
  void delete_WithNonExistentUserId_ShouldSucceed() {
    // Given
    given(userRepository.existsById(userId)).willReturn(false);
    willDoNothing().given(userRepository).deleteById(userId);

    // When
    userService.delete(userId);

    // Then
    then(userRepository).should().existsById(userId);
    then(userRepository).should().deleteById(userId);
  }

  @Test
  @DisplayName("사용자 삭제 실패 - 존재하는 사용자")
  void delete_WithExistingUser_ShouldThrowException() {
    // Given
    given(userRepository.existsById(userId)).willReturn(true);

    // When & Then
    assertThatThrownBy(() -> userService.delete(userId))
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage("사용자를 찾을 수 없습니다.");
  }
}