package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaAuditing
class UserRepositoryTest {

  public static final String USERNAME = "username";
  public static final String EMAIL = "test@email.com";
  public static final String PASSWORD = "password";
  @Autowired
  private UserRepository userRepository;

  @DisplayName("findByUsername: should return user when username exists")
  @Test
  void findByUsername() {
    //given
    User user = createUser();
    userRepository.save(user);
    // when
    Optional<User> foundUser = userRepository.findByUsername(USERNAME);
    // then
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getUsername()).isEqualTo(USERNAME);
  }

  @DisplayName("existsByEmail: should return true when email exists")
  @Test
  void testExistsByEmail() {
    //given
    User user = createUser();
    userRepository.save(user);
    // when
    boolean exists = userRepository.existsByEmail(EMAIL);
    // then
    assertThat(exists).isTrue();
  }

  @DisplayName("existsByUsername: should return true when username exists")
  @Test
  void testExistsByUsername() {
    //given
    User user = createUser();
    userRepository.save(user);
    // when
    boolean exists = userRepository.existsByUsername(USERNAME);
    // then
    assertThat(exists).isTrue();
  }

  @DisplayName("findAllWithProfileAndStatus: should return all users with profile and status")
  @Test
  void testMethodNameHere() {
    //given
    User user = createUser();
    UserStatus userStatus = new UserStatus(user, Instant.now());
    userRepository.save(user);
    // when
     List<User> users = userRepository.findAllWithProfileAndStatus();
    // then
    assertThat(users).hasSize(1);
    assertThat(users.get(0).getStatus()).isNotNull();
  }

  private static User createUser() {
    return new User(USERNAME, EMAIL, PASSWORD, null);
  }
}
