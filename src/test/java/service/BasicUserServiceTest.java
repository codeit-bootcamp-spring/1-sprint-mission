package service;

import com.sprint.mission.discodeit.dto.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class BasicUserServiceTest {

  UserRepository userRepository = new FileUserRepository();
  UserService userService = BasicUserService.getInstance(userRepository);

  @BeforeEach
  void beforeEach(){
    userRepository.clear();
  }

  @Test
  void invalidUserFormat(){
    User user = new User.UserBuilder("kiki1875", "1234", "wrong").build();

    Assertions.assertThatThrownBy(() -> userService.createUser(user))
        .isInstanceOf(UserValidationException.class);

    User user2 = new User.UserBuilder("kiki1875", "1234", "example@gmail.com")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();
    userService.createUser(user2);
  }

  @Test
  void invalidPwd(){
    User user1 = new User.UserBuilder("kiki1875", "1234", "example@gmail.com")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();

    userService.createUser(user1);

    Assertions.assertThatThrownBy(() -> userService.deleteUser(user1.getUUID(), "wrong"))
        .isInstanceOf(IllegalArgumentException.class);

    Assertions.assertThat(userService.readAllUsers()).hasSize(1);

    userService.deleteUser(user1.getUUID(), "1234");

    Assertions.assertThat(userService.readAllUsers().size()).isEqualTo(0);
  }

  @Test
  void updateUser(){
    User user1 = new User.UserBuilder("kiki1875", "1234", "example@gmail.com")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();

    userService.createUser(user1);

    UserUpdateDto updateDto = new UserUpdateDto(Optional.of("updatedName")
        ,Optional.empty()
        ,Optional.empty()
        ,Optional.empty()
        ,Optional.empty()
        ,Optional.empty()
        ,Optional.empty());

    userService.updateUser(user1.getUUID(), updateDto, "1234");

    Assertions.assertThat(userService.readUserById(user1.getUUID()).get().getUsername()).isEqualTo("updatedName");
  }
}
