package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private BinaryContentService binaryContentService;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private BasicUserService userService;

  @Nested
  @DisplayName("사용자 생성")
  class CreateTest {

    @Test
    @DisplayName("사용자 생성 성공")
    void create_user_success() {
      // given
      UserCreateRequest userRequest = new UserCreateRequest(
          "홍길동",
          "hong@codeit.com",
          "hong1234"
      );

      User savedUser = new User("홍길동", "hong@codeit.com", "hong1234", null);
      ReflectionTestUtils.setField(savedUser, "id", UUID.randomUUID());
      given(userRepository.save(any(User.class))).willReturn(savedUser);

      UserDto userDto = new UserDto(savedUser.getId(), "홍길동", "hong@codeit.com", null, true);
      given(userMapper.toDto(savedUser)).willReturn(userDto);

      // when
      UserDto result = userService.create(userRequest, null);

      // then
      assertEquals("홍길동", result.username());
      assertEquals("hong@codeit.com", result.email());
    }

    @Test
    @DisplayName("사용자 생성 실패 - username 중복")
    void create_user_failure_when_duplicate_username() {
      // given
      UserCreateRequest userRequest = new UserCreateRequest(
          "홍길동",
          "hong@codeit.com",
          "hong1234"
      );

      given(userRepository.existsByUsername("홍길동")).willReturn(true);

      // when & then
      assertThrows(DuplicateUsernameException.class,
          () -> userService.create(userRequest, null)
      );
    }

    @Test
    @DisplayName("사용자 생성 실패 - email 중복")
    void create_user_failure_when_duplicate_email() {
      // given
      UserCreateRequest userRequest = new UserCreateRequest(
          "홍길동",
          "hong@codeit.com",
          "hong1234"
      );

      given(userRepository.existsByEmail("hong@codeit.com")).willReturn(true);

      // when & then
      assertThrows(DuplicateEmailException.class,
          () -> userService.create(userRequest, null)
      );
    }
  }

  @Nested
  @DisplayName("사용자 생성")
  class UpdateTest {

    @Test
    @DisplayName("사용자 수정 성공")
    void update_user_success() {
      // given
      UUID userId = UUID.randomUUID();

      User existingUser = new User("홍길동", "hong@codeit.com", "hong1234", null);
      ReflectionTestUtils.setField(existingUser, "id", userId);

      UserUpdateRequest updateRequest = new UserUpdateRequest(
          "김철수",
          "chulsoo@codeit.com",
          "newPassword123"
      );

      given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));

      UserDto updatedDto = new UserDto(userId, "김철수", "chulsoo@codeit.com", null, true);
      given(userMapper.toDto(existingUser)).willReturn(updatedDto);

      // when
      UserDto result = userService.update(userId, updateRequest, null);

      // then
      assertEquals("김철수", result.username());
      assertEquals("chulsoo@codeit.com", result.email());
    }

    @Test
    @DisplayName("사용자 수정 실패 - user not found")
    void update_user_failure_when_user_not_found() {
      // given
      UUID nonExistentUserId = UUID.randomUUID();

      UserUpdateRequest updateRequest = new UserUpdateRequest(
          "김철수",
          "chulsoo@codeit.com",
          "newPassword123"
      );

      given(userRepository.findById(nonExistentUserId)).willReturn(Optional.empty());

      // when & then
      assertThrows(UserNotFoundException.class,
          () -> userService.update(nonExistentUserId, updateRequest, null)
      );
    }
  }

  @Nested
  @DisplayName("사용자 삭제")
  class DeleteTest {

    @Test
    @DisplayName("사용자 삭제 성공")
    void delete_user_success() {
      // given
      UUID userId = UUID.randomUUID();

      User existingUser = new User("홍길동", "hong@codeit.com", "hong1234", null);
      ReflectionTestUtils.setField(existingUser, "id", userId);

      given(userRepository.existsById(userId)).willReturn(true);

      // when
      userService.delete(userId);

      // then
      then(userRepository).should().deleteById(userId);
    }

    @Test
    @DisplayName("사용자 삭제 실패 - user not found")
    void delete_user_failure_when_user_not_found() {
      // given
      UUID nonExistentUserId = UUID.randomUUID();
      given(userRepository.existsById(nonExistentUserId)).willReturn(false);

      // when & then
      assertThrows(UserNotFoundException.class,
          () -> userService.delete(nonExistentUserId)
      );
    }
  }
}
