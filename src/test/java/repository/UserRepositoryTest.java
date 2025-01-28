package repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class UserRepositoryTest {
  private final UserRepository userRepository = FileUserRepository.getInstance();

  @BeforeEach
  void beforeEach(){
    userRepository.clear();
  }

  @Test
  void saveAndRead(){
    User user = new User.UserBuilder("username", "pwd123", "email@gmail.com")
        .description("description")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();

    userRepository.create(user);

    User user2 = userRepository.findById(user.getUUID()).get();

    Assertions.assertThat(user).isEqualTo(user2);
  }

  @Test
  void saveAndReadMultipleUsers(){
    User user = new User.UserBuilder("username", "pwd123", "email@gmail.com")
        .description("description")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();
    User user2 = new User.UserBuilder("username2", "pwd123", "email2@gmail.com")
        .description("description2")
        .nickname("kiki18752")
        .phoneNumber("01012345672")
        .build();

    userRepository.create(user);
    userRepository.create(user2);

    List<User> users = userRepository.findAll();

    Assertions.assertThat(users).containsExactly(user, user2);
  }

  @Test
  void updateUser(){
    User user = new User.UserBuilder("username", "pwd123", "email@gmail.com")
        .description("description")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();

    userRepository.create(user);

    User user2 = userRepository.findById(user.getUUID()).get();

    user2.setUsername("updatedUsername");

    userRepository.update(user2);

    Assertions.assertThat(userRepository.findAll()).hasSize(1);
    Assertions.assertThat(userRepository.findById(user.getUUID()).get().getUsername()).isEqualTo("updatedUsername");
  }

  @Test
  void deleteUser(){
    User user = new User.UserBuilder("username", "pwd123", "email@gmail.com")
        .description("description")
        .nickname("kiki1875")
        .phoneNumber("01012345678")
        .build();
    userRepository.create(user);

    userRepository.delete(user.getUUID());
    Assertions.assertThat(userRepository.findAll()).hasSize(0);
  }
}
