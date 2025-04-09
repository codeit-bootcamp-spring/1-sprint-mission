package com.sprint.mission.unit.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.BinaryContentMapperImpl;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.user.UserManagementService;
import com.sprint.mission.discodeit.service.user.UserManagementServiceImpl;
import com.sprint.mission.discodeit.service.user.UserService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class UserManagementServiceUnitTest {

  @Mock
  private UserService userService;
  @Mock
  private BinaryContentService binaryContentService;
  private BinaryContentMapper binaryContentMapper;
  private UserManagementService userManagementService;
  private User user;

  @BeforeEach
  void setUp() {
    binaryContentMapper = new BinaryContentMapperImpl();
    userManagementService = new UserManagementServiceImpl(userService, binaryContentService,
        binaryContentMapper);

    user = new User("testUser", "test@example.com", "pw", null, null);
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
  }

  @Nested
  class CreateUserTests {

    @Test
    void createUser_withProfile_success() throws IOException {
      // given
      MultipartFile profile = new MockMultipartFile("profile", "profile.jpg", "image/jpeg",
          "profile".getBytes());

      given(userService.saveUser(any(User.class))).willReturn(user);

      // when
      User savedUser = userManagementService.createUser(user, profile);

      // then
      assertThat(savedUser).isNotNull();
      assertThat(savedUser.getUsername()).isEqualTo("testUser");
      assertThat(savedUser.getProfile()).isNotNull();
      then(binaryContentService).should().save(any(), eq(profile.getBytes()));
      then(userService).should().saveUser(savedUser);
    }

    @Test
    void createUser_withProfile_fail_FileException() throws IOException {
      //given
      MultipartFile profile = new MockMultipartFile("profile", "profile.jpg", "image/jpeg",
          "profile".getBytes());

      given(binaryContentService.save(any(), eq(profile.getBytes()))).willThrow(
          FileException.class);

      //when
      //then
      assertThatThrownBy(() -> userManagementService.createUser(user, profile))
          .isInstanceOf(FileException.class);
    }

    @Test
    void createUser_withoutProfile_success() {

      //given
      given(userService.saveUser(any(User.class))).willReturn(user);

      //when
      User savedUser = userManagementService.createUser(user, null);
      //then

      assertThat(savedUser).isNotNull();
      assertThat(savedUser.getProfile()).isNull();
      assertThat(savedUser.getUsername()).isEqualTo("testUser");
      then(binaryContentService).shouldHaveNoInteractions();
    }

  }


  @Nested
  class UpdateUserTests {

    @Test
    void updateUser_withProfile_success() throws IOException {
      //given
      String userId = user.getId().toString();
      MultipartFile profile = new MockMultipartFile("profile", "profile.jpg", "image/jpeg",
          "profile".getBytes());
      User tmpUser = new User("updated", "update@gmail.com", "1234", null, null);

      given(userService.findUserById(any(String.class))).willReturn(user);
      given(userService.saveUser(any(User.class))).willReturn(user);

      //when
      User savedUser = userManagementService.updateUser(userId, tmpUser, profile);

      //then
      assertThat(savedUser).isNotNull();
      assertThat(savedUser.getUsername()).isEqualTo("updated");
      assertThat(savedUser.getProfile()).isNotNull();
      then(binaryContentService).should(times(1)).save(any(), eq(profile.getBytes()));
    }

    @Test
    void updateUser_withProfile_fail() throws IOException {
      //given

      String userId = user.getId().toString();
      MultipartFile profile = new MockMultipartFile("profile", "profile.jpg", "image/jpeg",
          "profile".getBytes());

      User tmpUser = new User("updated", "update@gmail.com", "1234", null, null);
      given(userService.findUserById(any(String.class))).willReturn(user);
      given(binaryContentService.save(any(), eq(profile.getBytes()))).willThrow(
          FileException.class);

      //when
      //then
      assertThatThrownBy(
          () -> userManagementService.updateUser(userId, tmpUser, profile)).isInstanceOf(
          FileException.class
      );
    }

    @Test
    void updateUser_withoutProfile_success() {
      String userId = user.getId().toString();
      User tmpUser = new User("updated", "update@gmail.com", "1234", null, null);

      given(userService.findUserById(userId)).willReturn(user);
      given(userService.saveUser(any(User.class))).willReturn(user);

      //when
      User result = userManagementService.updateUser(userId, tmpUser, null);

      //then
      assertThat(result).isEqualTo(user);
      assertThat(result.getUsername()).isEqualTo("updated");
      assertThat(result.getProfile()).isNull();
      then(userService).should().saveUser(user);
      then(binaryContentService).shouldHaveNoInteractions();
    }

    @Test
    void updateUser_findUserById_throwsException() {
      //given
      String userId = user.getId().toString();
      User tmpUser = new User("updated", "update@gmail.com", "1234", null, null);
      given(userService.findUserById(userId)).willThrow(UserNotFoundException.class);

      //when & then
      assertThatThrownBy(() -> userManagementService.updateUser(userId, tmpUser, null))
          .isInstanceOf(UserNotFoundException.class);
    }

  }

  @Nested
  class FindUserTests {

    @Test
    void findSingleUser_shouldCall_success() {
      // given
      String userId = user.getId().toString();
      given(userService.findUserById(userId)).willReturn(user);

      // when
      User result = userManagementService.findSingleUser(userId);

      //then
      assertThat(result).isEqualTo(user);
      then(userService).should().findUserById(userId);
    }

    @Test
    void findSingleUser_fail() {
      //given
      String userId = UUID.randomUUID().toString();
      given(userService.findUserById(userId))
          .willThrow(UserNotFoundException.class);

      // when & then
      assertThatThrownBy(() -> userManagementService.findSingleUser(userId))
          .isInstanceOf(UserNotFoundException.class);
      then(userService).should().findUserById(userId);
    }

    @Test
    void findAllUsers_success() {
      //given
      given(userService.findAllUsers()).willReturn(List.of(user));

      //when
      List<User> users = userManagementService.findAllUsers();

      //then

      assertThat(users).isNotEmpty();
      assertThat(users).hasSize(1);
      assertThat(users.get(0).getUsername()).isEqualTo("testUser");
      then(userService).should().findAllUsers();
    }

  }

  @Nested
  class DeleteUserTests {

    @Test
    void deleteUser_withProfile_shouldDeleteBinary() {
      // given
      String userId = user.getId().toString();

      BinaryContent profileBinaryContent = new BinaryContent("testProfile", 1L, "image/jpg");

      ReflectionTestUtils.setField(profileBinaryContent, "id", UUID.randomUUID());

      user.updateProfileImage(profileBinaryContent);
      given(userService.findUserById(userId)).willReturn(user);

      // when
      userManagementService.deleteUser(userId);

      // then
      then(userService).should(times(1)).deleteUser(userId);
      then(binaryContentService).should(times(1)).delete(profileBinaryContent.getId().toString());
    }

    @Test
    void deleteUser_withoutProfile_shouldSkipBinary() {
      // given
      String userId = user.getId().toString();
      given(userService.findUserById(userId)).willReturn(user);

      // when
      userManagementService.deleteUser(userId);

      // then
      then(userService).should().deleteUser(userId);
      then(binaryContentService).shouldHaveNoInteractions();
    }

    @Test
    void deleteUser_findUserById_throwsException() {
      //given
      String userId = UUID.randomUUID().toString();
      given(userService.findUserById(userId))
          .willThrow(UserNotFoundException.class);

      // when & then
      assertThatThrownBy(() -> userManagementService.deleteUser(userId)).isInstanceOf(
          UserNotFoundException.class);
      then(userService).should().findUserById(userId);
      then(userService).shouldHaveNoMoreInteractions();
      then(binaryContentService).shouldHaveNoInteractions();
    }
  }


}
