package com.sprint.mission.discodeit.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.LocalBinaryContentStorage;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
  private UserStatusRepository userStatusRepository;
  @Mock
  private UserMapper userMapper;
  @Mock
  private LocalBinaryContentStorage binaryContentStorage;
  @Mock
  private BinaryContentRepository binaryContentRepository;

  @InjectMocks
  private BasicUserService userService;

  @Nested
  @DisplayName("create() 메서드")
  class CreateUser {

    @Test
    @DisplayName("사용자 생성 성공 (프로필 사진 없을 때)")
    void createSuccess() {
      //given
      UserCreateRequest request = new UserCreateRequest("kbh", "kbh@example.com", "1234");
      User user = new User("kbh", "kbh@example.com", "1234", null);
      UserDto dto = new UserDto(
          UUID.randomUUID(),
          Instant.now(),
          Instant.now(),
          "kbh",
          "kbh@example.com",
          null,
          false
      );

      given(userRepository.existsByEmail(request.email())).willReturn(false);
      given(userRepository.existsByUsername(request.username())).willReturn(false);
      given(userRepository.save(any(User.class))).willReturn(user);
      given(userMapper.toDto(user)).willReturn(dto);

      //when
      UserDto result = userService.create(request, Optional.empty());

      //then
      Assertions.assertThat(result.username()).isEqualTo("kbh");
      Assertions.assertThat(result.email()).isEqualTo("kbh@example.com");

      then(userRepository).should().save(any());
      then(userStatusRepository).should().save(any(UserStatus.class));
    }

    @Test
    @DisplayName("사용자 생성 성공 (프로필 사진 포함)")
    void createWithProfileSuccess() {
      //given
      String username = "kbh";
      String email = "kbh@example.com";
      String password = "1234";
      UUID storageId = UUID.randomUUID();

      // 프로필 파일
      BinaryContentCreateRequest profileRequest = new BinaryContentCreateRequest(
          "profile.jpg",
          "image/jpeg",
          "imageData".getBytes()
      );
      UserCreateRequest request = new UserCreateRequest(username, email, password);
      User user = new User(username, email, password, null);
      User savedUser = new User(username, email, password,
          new BinaryContent(storageId, "profile.jpg", 14L, "image/jpeg"));

      given(userRepository.existsByEmail(request.email())).willReturn(false);
      given(userRepository.existsByUsername(request.username())).willReturn(false);
      given(binaryContentStorage.put(null, profileRequest.bytes())).willReturn(storageId);
      given(userRepository.save(any(User.class))).willReturn(savedUser);
      given(userMapper.toDto(any(User.class))).willReturn(
          new UserDto(UUID.randomUUID(), Instant.now(), Instant.now(), username, email, storageId,
              false)
      );

      //when
      UserDto result = userService.create(request, Optional.of(profileRequest));

      //then
      Assertions.assertThat(result.username()).isEqualTo(username);
      Assertions.assertThat(result.email()).isEqualTo(email);
      Assertions.assertThat(result.profileId()).isEqualTo(storageId);

      then(binaryContentStorage).should().put(null, profileRequest.bytes());
      then(userRepository).should().save(any(User.class));
    }

    @Test
    @DisplayName("사용자 생성 실패 (중복 사용자 이름)")
    void createFailWithDuplicateUsername() {
      //given
      UserCreateRequest request = new UserCreateRequest("kbh", "kbh@example.com", "1234");
      given(userRepository.existsByEmail(request.email())).willReturn(false);
      given(userRepository.existsByUsername(request.username())).willReturn(true);

      //when then
      Assertions.assertThatThrownBy(() -> userService.create(request, Optional.empty()))
          .isInstanceOf(DuplicateUsernameException.class)
          .hasMessageContaining("이미 존재하는 사용자 이름입니다.");

      then(userRepository).should().existsByUsername("kbh");
      then(userRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("사용자 생성 실패 (중복 이메일)")
    void createFailWithDuplicateEmail() {
      //given
      UserCreateRequest request = new UserCreateRequest("kbh", "kbh@example.com", "1234");
      given(userRepository.existsByEmail(request.email())).willReturn(true);

      //when then
      Assertions.assertThatThrownBy(() -> userService.create(request, Optional.empty()))
          .isInstanceOf(DuplicateEmailException.class)
          .hasMessageContaining("이미 존재하는 이메일입니다.");

      then(userRepository).should().existsByEmail("kbh@example.com");
      then(userRepository).should(never()).save(any());
    }
  }

  @Nested
  @DisplayName("update() 메서드")
  class UpdateUser {

    @Test
    @DisplayName("사용자 정보 수정 성공")
    void updateSuccess() {
      //given
      UUID userId = UUID.randomUUID();
      UserUpdateRequest request = new UserUpdateRequest("new_kbh", "new@email.com", "5678");
      User user = new User("kbh", "kbh@example.com", "1234", null);

      given(userRepository.findById(userId)).willReturn(Optional.of(user));
      given(userRepository.existsByEmail(request.newEmail())).willReturn(false);
      given(userRepository.existsByUsername(request.newUsername())).willReturn(false);
      given(userRepository.save(any(User.class))).willReturn(user);

      UserDto expectedDto = new UserDto(userId, Instant.now(), Instant.now(), "new_kbh",
          "new@email.com", null, false);
      given(userMapper.toDto(user)).willReturn(expectedDto);
      //when
      UserDto result = userService.update(userId, request, Optional.empty());
      //then
      Assertions.assertThat(result.username()).isEqualTo("new_kbh");
      Assertions.assertThat(result.email()).isEqualTo("new@email.com");

      then(userRepository).should().save(any(User.class));

    }

    @Test
    @DisplayName("프로필 이미지가 있는 사용자 정보 수정 성공")
    void updateWithProfileChangeSuccess() {
      // given
      UUID userId = UUID.randomUUID();
      UUID newStorageId = UUID.randomUUID();

      BinaryContent oldProfile = new BinaryContent(
          UUID.randomUUID(), "old.jpg", 1234L, "image/jpeg"
      );

      // 수정할 요청
      UserUpdateRequest request = new UserUpdateRequest("new_kbh", "new@email.com", "5678");

      BinaryContentCreateRequest newProfileRequest = new BinaryContentCreateRequest(
          "new.jpg", "image/jpeg", "newData".getBytes()
      );

      // 기존 유저
      User user = new User("old_kbh", "old@email.com", "1234", oldProfile);

      // 저장된 새 프로필
      BinaryContent newProfile = new BinaryContent(
          newStorageId, "new.jpg", 7L, "image/jpeg"
      );

      User updatedUser = new User("new_kbh", "new@email.com", "5678", newProfile);

      given(userRepository.findById(userId)).willReturn(Optional.of(user));
      given(userRepository.existsByEmail(request.newEmail())).willReturn(false);
      given(userRepository.existsByUsername(request.newUsername())).willReturn(false);
      given(binaryContentStorage.put(any(), any(byte[].class))).willReturn(newStorageId);
      given(userRepository.save(any(User.class))).willReturn(updatedUser);
      given(userMapper.toDto(any(User.class))).willReturn(
          new UserDto(userId, Instant.now(), Instant.now(), "new_kbh", "new@email.com",
              newStorageId, false)
      );

      // when
      UserDto result = userService.update(userId, request, Optional.of(newProfileRequest));

      // then
      Assertions.assertThat(result.username()).isEqualTo("new_kbh");
      Assertions.assertThat(result.email()).isEqualTo("new@email.com");
      Assertions.assertThat(result.profileId()).isEqualTo(newStorageId);

      then(binaryContentRepository).should().delete(oldProfile);
      then(binaryContentStorage).should().put(null, newProfileRequest.bytes());
      then(userRepository).should().save(any(User.class));
    }
  }
}
