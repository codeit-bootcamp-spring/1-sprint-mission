package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private UserStatusRepository userStatusRepository;

  @InjectMocks
  private BasicUserService userService;

  private User testUser;
  private BinaryContent testBinaryContent;
  private CreateUserRequest testRequest;
  private CreateBinaryContentRequest testFileRequest;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    String username = "username1";
    String email = "email1@email.com";
    String password = "password1";
    byte[] dummyBytes = "Hello, World!".getBytes();

    testBinaryContent = new BinaryContent("test.txt", "text/plain", "/dummy/path");
    testUser = new User(username, email, password);
    testUser.setProfileImage(testBinaryContent);

    testRequest = new CreateUserRequest(username, email, password);
    testFileRequest = new CreateBinaryContentRequest("filename", ".jpg", dummyBytes);
  }

  @Test
  @DisplayName("유저를 생성한다.")
  void testCreateUser() {
    // given
    when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(testUser);
    when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(testBinaryContent);

    // when
    UserResponse savedUser = userService.createUser(testRequest,
        Optional.ofNullable(testFileRequest));

    // then
    assertThat(savedUser).isNotNull();
    assertThat(savedUser.username()).isEqualTo(testUser.getUsername());
    assertThat(savedUser.email()).isEqualTo(testUser.getEmail());
    assertThat(savedUser.profile()).isEqualTo(testBinaryContent.getId());

    verify(userRepository, times(1)).save(any(User.class));
  }

  @Test
  @DisplayName("유저를 생성하는데 중복된 이름이 들어온다")
  void testCreateUserByDuplicateName() {
    // given
    when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(true);

    // when
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> userService.createUser(testRequest, Optional.empty()));

    // then
    assertThat(exception.getMessage()).isEqualTo("이미 사용 중인 username 또는 email입니다.");

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("유저를 생성하고 단일 조회를 한다")
  void testGetUserById() {
    // given

    // when
    when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(testUser);
    when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(testBinaryContent);
    when(userRepository.findById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));

    UserResponse savedUser = userService.createUser(testRequest, Optional.empty());
    Optional<UserResponse> result = userService.findUserById(savedUser.id());

    // then
    assertThat(result).isPresent();
  }

  @Test
  @DisplayName("유저를 생성하고 업데이트한 후 조회한다")
  void testUpdateUser() {
    // given
    String updateUsername1 = "updateUsername1";
    UpdateUserRequest request2 = new UpdateUserRequest(updateUsername1);

    // when
    when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(testUser);
    when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(testBinaryContent);
    when(userRepository.findById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));

    UserResponse savedUser = userService.createUser(testRequest, Optional.empty());
    Optional<UserResponse> result = userService.findUserById(savedUser.id());

    // then
    assertThat(result).isPresent();
    assertThat(result.get().username()).isEqualTo("username1");
    assertThat(result.get().profile()).isEqualTo(testBinaryContent.getId());

    // when
    Optional<UserResponse> updateResult = userService.updateUser(testUser.getId(), request2,
        Optional.empty());

    // then
    assertThat(updateResult).isPresent();
    assertThat(updateResult.get().username()).isEqualTo(updateUsername1);
    assertThat(updateResult.get().profile()).isEqualTo(testBinaryContent.getId());
  }

  @Test
  @DisplayName("유저를 생성하고 삭제한 후 확인한다")
  void testDeleteUser() {
    // given

    // when
    when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(testUser);
    when(binaryContentRepository.save(any(BinaryContent.class))).thenReturn(testBinaryContent);
    when(userRepository.findById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));

    UserResponse savedUser = userService.createUser(testRequest, Optional.empty());
    Optional<UserResponse> result = userService.findUserById(savedUser.id());

    // then
    assertThat(result).isPresent();
  }

  @Test
  @DisplayName("존재하지 않는 유저를 조회하면 Optional.empty()를 반환한다")
  void testFindNonExistingUser() {
    // given
    UUID nonExistingUserId = UUID.randomUUID();

    // when
    when(userRepository.findById(nonExistingUserId)).thenReturn(null);
    Optional<UserResponse> result = userService.findUserById(nonExistingUserId);

    // then
    assertThat(result).isEmpty();
  }
}
