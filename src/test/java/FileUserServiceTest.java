import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.USER_FILE;

public class FileUserServiceTest {

  private final UserService userService = FileUserService.getInstance();

  @BeforeEach
  void setUp() {

    File file = new File(USER_FILE);
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  void writeAndReadFromFile() {

    User user1 = null;
    User user2 = null;
    try {
      user1 = userService.createUser(
          new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
              .phoneNumber("01012345678")
              .nickname("asdfff")
              .description("testDiscription")
              .build());

      user2 = userService.createUser(
          new User.UserBuilder("kiki1872", "123222", "kevinheo2@gmail.com")
              .phoneNumber("01012345677")
              .nickname("asdfff2")
              .description("testDiscription2")
              .build());

    } catch (UserValidationException e) {
      throw new RuntimeException(e);
    }

    List<User> users = userService.readAllUsers();
    Assertions.assertThat(users).containsExactly(user1, user2);
  }

  @Test
  void duplicateUserNotAllowed() {
    try {
      userService.createUser(
          new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
              .phoneNumber("01012345678")
              .nickname("asdfff")
              .description("testDiscription")
              .build());


    } catch (UserValidationException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertThatThrownBy(() ->
            userService.createUser(
                new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
                    .phoneNumber("01012345678")
                    .nickname("asdfff")
                    .description("testDiscription")
                    .build()))
        .isInstanceOf(UserValidationException.class);
  }

  @Test
  void readUserById() {
    User user = null;
    try {
      user = userService.createUser(
          new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
              .phoneNumber("01012345678")
              .nickname("asdfff")
              .description("testDiscription")
              .build());


    } catch (UserValidationException e) {
      throw new RuntimeException(e);
    }

    Assertions.assertThat(user).isEqualTo(userService.readUserById(user.getUUID()).get());
  }

  @Test
  void test(){
    UserService us = JCFUserService.getInstance();

    User user = us.createUser(
        new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
            .phoneNumber("01012345678")
            .nickname("asdfff")
            .description("testDiscription")
            .build());

    boolean t = PasswordEncryptor.checkPassword("123422", user.getPassword());
    Assertions.assertThat(t).isTrue();
  }

  @Test
  void updateUserAndVerifySameObject() {
    User user = null;
    try {
      user = userService.createUser(
          new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
              .phoneNumber("01012345678")
              .nickname("asdfff")
              .description("testDiscription")
              .build());


    } catch (UserValidationException e) {
      throw new RuntimeException(e);
    }
    String userId = user.getUUID();

    UserUpdateDto updateDto = new UserUpdateDto(Optional.of("new nickname")
        , Optional.empty()
        , Optional.empty()
        , Optional.empty()
        , Optional.empty()
        , Optional.empty()
        , Optional.empty());

    userService.updateUser(userId, updateDto, "123422");

    User afterUpdateUser = userService.readUserById(userId).get();

    Assertions.assertThat(PasswordEncryptor.checkPassword("123422", user.getPassword())).isTrue();
    Assertions.assertThat(user).isEqualTo(afterUpdateUser);
    Assertions.assertThat(afterUpdateUser.getUsername()).isEqualTo("new nickname");
  }

  @Test
  void deleteUser(){
    User user = null;
    try {
      user = userService.createUser(
          new User.UserBuilder("kiki1875", "123422", "kevinheo1@gmail.com")
              .phoneNumber("01012345678")
              .nickname("asdfff")
              .description("testDiscription")
              .build());


    } catch (UserValidationException e) {
      throw new RuntimeException(e);
    }
    String userId = user.getUUID();

    userService.deleteUser(user.getUUID(), "123422");
    List<User> users = userService.readAllUsers();
    Assertions.assertThat(users.size()).isEqualTo(0);
  }
}
