package com.sprint.mission.discodeit.service.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
import com.sprint.mission.discodeit.entity.status.user.UserStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserCreateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.user.dto.UserUpdateResponse;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.status.UserStateService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.persistence.EntityManager;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserStateService userStateService;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private EntityManager entityManager;

  @InjectMocks
  private BasicUserService userService;

  @BeforeEach
  void init() throws NoSuchFieldException, IllegalAccessException {
    Field em = BasicUserService.class.getDeclaredField("em");
    em.setAccessible(true);
    em.set(userService, entityManager);
  }


  @Test
  @DisplayName("user create 성공")
  void createUser() throws IOException {

    UserCreateRequest request = new UserCreateRequest("user", "user@mail.com", "1234");
    User savedUser = new User("user", "user@mail.com", "1234", null, null);
    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    UserStatus userStatus = new UserStatus(savedUser, Instant.now());
    when(userStateService.create(any(UserStatus.class))).thenReturn(userStatus);

    UserCreateResponse joinUser = userService.join(request, null);

    assertNotNull(joinUser);
    verify(userRepository).save(any(User.class));
    verify(userStateService).create(any(UserStatus.class));
  }

  @Test
  @DisplayName("profile img와 함께 user create 성공")
  void createUserWithProfileImg() throws IOException {
    UserCreateRequest request = new UserCreateRequest("test", "test@mail.com", "1234");

    MultipartFile mockProfile = mock(MultipartFile.class);
    byte[] content = "test txt".getBytes();
    when(mockProfile.getBytes()).thenReturn(content);
    when(mockProfile.getContentType()).thenReturn("text/plain");
    when(mockProfile.getOriginalFilename()).thenReturn("test.txt");
    when(mockProfile.getSize()).thenReturn(30L);
    when(mockProfile.getName()).thenReturn("test.txt");

    BinaryContent binaryContent = new BinaryContent(mockProfile.getName(), mockProfile.getSize(),
        mockProfile.getContentType());
    when(binaryContentStorage.put(any(), any())).thenReturn(binaryContent.getId());

    User user = new User("test", "test@mail.com", "1234", binaryContent, null);
    when(userRepository.save(any(User.class))).thenReturn(user);

    UserStatus userStatus = new UserStatus(user, Instant.now());
    when(userStateService.create(any(UserStatus.class))).thenReturn(userStatus);

    UserCreateResponse joinUser = userService.join(request, mockProfile);

    assertNotNull(joinUser);
    assertNotNull(joinUser.profile());

    verify(userRepository).save(any(User.class));
    verify(binaryContentStorage).put(any(), any());
    verify(userStateService).create(any(UserStatus.class));
  }

  @Test
  @DisplayName("user create 실패 - 중복 이름 저장 시도")
  void createUserFail_name() throws Exception {

    UserCreateRequest request = new UserCreateRequest("test", "test@mail.com", "1234");

    User user = new User("test", "test@mail.com", "1234", null, null);

    when(userRepository.findUserByUsername(request.getUsername())).thenReturn(user);

    assertThrows(DuplicateUsernameException.class, () -> {
      userService.join(request, null);
    });

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("user create 실패 - 중복 이메일 저장 시도")
  void createUserFail_email() throws Exception {

    UserCreateRequest request = new UserCreateRequest("test", "test@mail.com", "1234");
    User user = new User("test", "test@mail.com", "1234", null, null);

    when(userRepository.findUserByEmail(request.getEmail())).thenReturn(user);

    assertThrows(DuplicateEmailException.class, () -> {
      when(userService.join(request, null));
    });

    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  @DisplayName("전체 유저 목록 조회")
  void findAllUser() throws Exception {
    User savedUser1 = new User("user1", "user1@mail.com", "1234", null, null);
    UserStatus status1 = new UserStatus(savedUser1, Instant.now());
    User savedUser2 = new User("user2", "user2@mail.com", "1234", null, null);
    UserStatus status2 = new UserStatus(savedUser2, Instant.now());
    savedUser1.changeUserStatus(status1);
    savedUser2.changeUserStatus(status2);

    List<User> userList = List.of(savedUser1, savedUser2);
    when(userRepository.findUsers()).thenReturn(userList);

    List<UserCreateResponse> users = userService.findAll();

    assertNotNull(users);
    assertEquals(2, users.size());
    assertEquals("user1", users.get(0).username());
    assertEquals("user2", users.get(1).username());
  }

  @Test
  @DisplayName("유저 삭제 성공")
  void removeUser() throws Exception {
    UUID userId = UUID.randomUUID();
    User savedUser1 = new User("user1", "user1@mail.com", "1234", null, null);
    setUserId(savedUser1, userId);

    when(userRepository.existsById(userId)).thenReturn(true);

    doNothing().when(userRepository).deleteById(userId);

    userService.delete(userId);

    verify(userRepository).existsById(userId);
    verify(userRepository).deleteById(userId);
  }

  @Test
  @DisplayName("유저 삭제 실패")
  void removeUserFail() throws Exception {
    UUID notExistsUserID = UUID.randomUUID();

    when(userRepository.existsById(notExistsUserID)).thenReturn(false);

    assertThrows(UserNotFoundException.class, () -> userService.delete(notExistsUserID));

    verify(userRepository).existsById(notExistsUserID);
    verify(userRepository, never()).removeUserById(any(UUID.class));
  }

  @Test
  @DisplayName("유저 정보 수정 성공")
  void updateUser() throws Exception {
    UserUpdateRequest request = new UserUpdateRequest("newName", "newmail@mail.com",
        "newpassowrd");
    User findUser = new User("newName", "newmail@mail.com", "newpassowrd", null, null);

    doNothing().when(entityManager).flush();
    doNothing().when(entityManager).clear();

    when(userRepository.findById(findUser.getId())).thenReturn(Optional.of(findUser));

    UserUpdateResponse updatedUser = userService.update(findUser.getId(), request, null);

    assertNotNull(updatedUser);
    assertEquals("newName", updatedUser.username());
    assertEquals("newmail@mail.com", updatedUser.email());

    verify(userRepository).findById(findUser.getId());
    verify(entityManager).flush();
    verify(entityManager).clear();
  }

  @Test
  @DisplayName("유저 정보 업데이트 실패 - 중복")
  void updateUserFailCauseUsername() throws Exception {
    UUID userId = UUID.randomUUID();
    UserUpdateRequest request = new UserUpdateRequest("newName", "newmail@mail.com",
        "newpassowrd");
    User oldUser = new User("oldName", "oldmail@mail.com", "oldpassword", null, null);
    setUserId(oldUser, userId);

    when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));
    doThrow(new ConstraintViolationException("duplicate name", new SQLException("name"), "name"))
        .when(entityManager).flush();

    assertThrows(ConstraintViolationException.class, () -> {
      userService.update(oldUser.getId(), request, null);
    });

    verify(entityManager).flush();
  }

  @Test
  @DisplayName("유저 상태 업데이트")
  void updateUserStatus() throws Exception {
    User user = createUserWithUSerStatus();

    Instant now = Instant.now();
    UserStatusUpdateRequest request = new UserStatusUpdateRequest(now);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    UserStatusUpdateResponse response = userService.updateOnlineStatus(user.getId(),
        request);

    assertNotNull(response);
    assertEquals(now, user.getStatus().getLastActiveAt());

    verify(userRepository).findById(user.getId());
  }

  private User createUserWithUSerStatus() {
    User user = new User("user", "user@mail.com", "password1234", null, null);
    user.changeUserStatus(new UserStatus(user, null));
    UUID userId = UUID.randomUUID();
    setUserId(user, userId);

    return user;
  }

  private void setUserId(User user, UUID userId) {
    Field idField = ReflectionUtils.findField(User.class, "id");
    ReflectionUtils.makeAccessible(idField);
    ReflectionUtils.setField(idField, user, userId);
  }
}