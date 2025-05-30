package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.file.FileCreateException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserEmailDuplicateException;
import com.sprint.mission.discodeit.exception.user.UserNameDuplicateException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.BinaryContentMapperImpl;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.UserMapperImpl;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  UserRepository userRepository;
  @Mock
  BinaryContentRepository binaryContentRepository;
  @Mock
  BinaryContentStorage binaryContentStorage;

  @Spy
  @InjectMocks
  UserMapper userMapper = new UserMapperImpl();
  @Spy
  BinaryContentMapper binaryContentMapper = new BinaryContentMapperImpl();

  @InjectMocks
  UserService userService;

  String email = "test@test.com";
  String username = "username";
  String password = "password";
  UserCreateRequest userCreateRequest = new UserCreateRequest(email, username, password);
  MultipartFile profile = mock(MultipartFile.class);

  @Test
  @DisplayName("user 생성 - proflie 없는 경우")
  void createUser() {
    //given
    User user = User.create(username, email, password);
    UUID userId = UUID.randomUUID();
    ReflectionTestUtils.setField(user, "id", userId);

    given(userRepository.save(any(User.class))).willReturn(user);
    given(userRepository.existsByEmail(any())).willReturn(false);
    given(userRepository.existsByUsername(any())).willReturn(false);

    //when
    UserResponse response = userService.createUser(userCreateRequest, null);

    //then
    assertThat(response.id()).isEqualTo(userId);
    assertThat(response.email()).isEqualTo(email);
    assertThat(response.username()).isEqualTo(username);
    assertThat(response.profile()).isNull();

    verify(binaryContentRepository, never()).save(any(BinaryContent.class));
    verify(binaryContentStorage, never()).put(any(UUID.class), any(byte[].class));
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("user 생성 - proflie 있는 경우")
  void createUserWithProfile() throws IOException {
    //given
    User user = User.create(username, email, password);
    UUID userId = UUID.randomUUID();
    ReflectionTestUtils.setField(user, "id", userId);

    given(profile.isEmpty()).willReturn(false);
    given(profile.getSize()).willReturn(1024L);
    given(profile.getOriginalFilename()).willReturn("profile.jpg");
    given(profile.getBytes()).willReturn("new byte[]".getBytes());
    BinaryContent content = BinaryContent.create(profile.getSize(), profile.getName(),
        profile.getContentType());
    ReflectionTestUtils.setField(content, "id", UUID.randomUUID());

    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(content);
    given(binaryContentStorage.put(any(UUID.class), any(byte[].class))).willReturn(content.getId());
    given(userRepository.save(any(User.class))).willReturn(user);
    given(userRepository.existsByEmail(any())).willReturn(false);
    given(userRepository.existsByUsername(any())).willReturn(false);

    //when
    UserResponse response = userService.createUser(userCreateRequest, profile);

    //then
    assertThat(response.id()).isEqualTo(userId);
    assertThat(response.email()).isEqualTo(email);
    assertThat(response.username()).isEqualTo(username);
    assertThat(response.profile()).isNotNull();
    assertThat(response.profile().id()).isEqualTo(content.getId());

    verify(binaryContentRepository).save(any(BinaryContent.class));
    verify(binaryContentStorage).put(any(UUID.class), any(byte[].class));
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("user 생성 실패 - 중복된 이메일")
  void failCreateUserWithDuplicateEmail() {
    given(userRepository.existsByEmail(email)).willReturn(true);

    assertThatThrownBy(() -> userService.createUser(userCreateRequest, null))
        .isInstanceOf(UserAlreadyExistException.class)
        .hasMessage(ErrorCode.USER_ALREADY_EXIST.getMessage());
  }

  @Test
  @DisplayName("user 생성 실패 - 중복된 이름")
  void failCreateUserWithDuplicateUsername() {
    given(userRepository.existsByUsername(username)).willReturn(true);

    assertThatThrownBy(() -> userService.createUser(userCreateRequest, null))
        .isInstanceOf(UserAlreadyExistException.class)
        .hasMessage(ErrorCode.USER_ALREADY_EXIST.getMessage());
  }

  @Test
  @DisplayName("파일 생성 실패")
  void failCreateProfile() throws IOException {
    given(profile.isEmpty()).willReturn(false);
    given(profile.getSize()).willReturn(1024L);
    given(profile.getOriginalFilename()).willReturn("profile.jpg");
    given(profile.getBytes()).willThrow(IOException.class);
    BinaryContent content = BinaryContent.create(profile.getSize(), profile.getName(),
        profile.getContentType());
    ReflectionTestUtils.setField(content, "id", UUID.randomUUID());

    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(content);

    assertThatThrownBy(() -> userService.createUser(userCreateRequest, profile))
        .isInstanceOf(FileCreateException.class)
        .hasMessage(ErrorCode.FILE_CREATE_FAIL.getMessage());
  }

  @Test
  @DisplayName("user 수정 - profile 없는 경우")
  void updateUser() {
    String newEmail = "new@new.com";
    String newUsername = "newUsername";
    String newPassword = "newPassword";
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newEmail, newUsername, newPassword);

    User user = User.create(username, email, password);
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
    given(userRepository.findByIdWithProfile(user.getId())).willReturn(Optional.of(user));
    given(userRepository.existsByEmail(newEmail)).willReturn(false);
    given(userRepository.existsByUsername(newUsername)).willReturn(false);

    //when
    UserResponse userResponse = userService.updateUser(user.getId(), userUpdateRequest, null);

    //then
    assertThat(userResponse.id()).isEqualTo(user.getId());
    assertThat(userResponse.profile()).isNull();
    assertThat(userResponse.username()).isEqualTo(newUsername);
    assertThat(userResponse.email()).isEqualTo(newEmail);

    verify(binaryContentStorage, never()).delete(any(UUID.class));
  }

  @Test
  @DisplayName("user 수정 - profile 있는 경우")
  void updateUserWithProfile() throws IOException {
    String newEmail = "new@new.com";
    String newUsername = "newUsername";
    String newPassword = "newPassword";
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newEmail, newUsername, newPassword);

    User user = User.create(username, email, password);
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
    given(userRepository.findByIdWithProfile(user.getId())).willReturn(Optional.of(user));

    given(profile.isEmpty()).willReturn(false);
    given(profile.getSize()).willReturn(1024L);
    given(profile.getOriginalFilename()).willReturn("profile.jpg");
    given(profile.getBytes()).willReturn("new byte[]".getBytes());
    BinaryContent content = BinaryContent
        .create(profile.getSize(), profile.getName(), profile.getContentType());
    ReflectionTestUtils.setField(content, "id", UUID.randomUUID());
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(content);

    given(userRepository.existsByEmail(newEmail)).willReturn(false);
    given(userRepository.existsByUsername(newUsername)).willReturn(false);

    //when
    UserResponse userResponse = userService.updateUser(user.getId(), userUpdateRequest, profile);

    //then
    assertThat(userResponse.id()).isEqualTo(user.getId());
    assertThat(userResponse.username()).isEqualTo(newUsername);
    assertThat(userResponse.email()).isEqualTo(newEmail);
    assertThat(userResponse.profile()).isNotNull();
    assertThat(userResponse.profile().id()).isEqualTo(content.getId());
    assertThat(userResponse.profile().size()).isEqualTo(content.getSize());
    assertThat(userResponse.profile().fileName()).isEqualTo(content.getFileName());
    assertThat(userResponse.profile().contentType()).isEqualTo(content.getContentType());
  }

  @Test
  @DisplayName("user 수정 실패 - 이메일이 중복")
  void failUpdateUserEmail() {
    String newEmail = "new@new.com";
    String newUsername = "newUsername";
    String newPassword = "newPassword";
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newEmail, newUsername, newPassword);

    User user = User.create(username, email, password);
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
    given(userRepository.findByIdWithProfile(user.getId())).willReturn(Optional.of(user));
    given(userRepository.existsByEmail(newEmail)).willReturn(true);

    assertThatThrownBy(() -> userService.updateUser(user.getId(), userUpdateRequest, null))
        .isInstanceOf(UserEmailDuplicateException.class);
  }

  @Test
  @DisplayName("user 수정 실패 - 이름이 중복")
  void failUpdateUserUsername() {
    String newEmail = "new@new.com";
    String newUsername = "newUsername";
    String newPassword = "newPassword";
    UserUpdateRequest userUpdateRequest = new UserUpdateRequest(newEmail, newUsername, newPassword);

    User user = User.create(username, email, password);
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
    given(userRepository.findByIdWithProfile(user.getId())).willReturn(Optional.of(user));
    given(userRepository.existsByUsername(newUsername)).willReturn(true);

    assertThatThrownBy(() -> userService.updateUser(user.getId(), userUpdateRequest, null))
        .isInstanceOf(UserNameDuplicateException.class);
  }

  @Test
  @DisplayName("user 삭제 - user가 없는 경우")
  void deleteUser() {
    UUID userId = UUID.randomUUID();
    given(userRepository.findByIdWithProfile(any(UUID.class))).willReturn(
        Optional.empty());

    //when
    userService.deleteUser(userId);

    //then
    verify(binaryContentStorage, never()).delete(any(UUID.class));
    verify(userRepository, never()).delete(any(User.class));
  }

  @Test
  @DisplayName("user 삭제 - user가 있는 경우")
  void deleteExistUser() {
    UUID userId = UUID.randomUUID();
    User user = User.create(username, email, password);
    ReflectionTestUtils.setField(user, "id", userId);
    given(userRepository.findByIdWithProfile(userId)).willReturn(Optional.of(user));

    //when
    userService.deleteUser(userId);

    //then
    verify(binaryContentStorage, never()).delete(any(UUID.class));
    verify(userRepository).delete(any(User.class));
  }
}