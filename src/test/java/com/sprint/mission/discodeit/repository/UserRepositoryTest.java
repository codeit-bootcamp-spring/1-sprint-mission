package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  private User createUser(String username, String email) {
    BinaryContent profile = new BinaryContent("test.png", 1024L, "image/png");
    User user = new User(username, email, "password123", profile);
    new UserStatus(user, Instant.now());
    return userRepository.save(user);
  }

  @Test
  @DisplayName("findByUsername 성공 케이스")
  void testFindByUsernameSuccess() {
    // given
    User saved = createUser("testuser", "test@example.com");

    // when
    Optional<User> found = userRepository.findByUsername("testuser");

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("test@example.com");
  }

  @Test
  void testFindByUsernameFail() {
    // given
    createUser("existuser", "exist@test.com");

    // when
    Optional<User> result = userRepository.findByUsername("notExistUser");

    // then
    assertThat(result).isNotPresent(); // 또는 isEmpty()
  }

  @Test
  void testExistsByEmail() {
    // given
    createUser("emailuser", "email@test.com");

    // when & then
    assertThat(userRepository.existsByEmail("email@test.com")).isTrue();
    assertThat(userRepository.existsByEmail("wrong@test.com")).isFalse();
  }

  @Test
  void testExistsByUsername() {
    // given
    createUser("uniqueuser", "unique@test.com");

    // when & then
    assertThat(userRepository.existsByUsername("uniqueuser")).isTrue();
    assertThat(userRepository.existsByUsername("nouser")).isFalse();
  }

  @Test
  void testFindAllWithProfileAndStatus() {
    // given
    createUser("user1", "user1@test.com");
    createUser("user2", "user2@test.com");

    // when
    List<User> allUsers = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(allUsers).hasSize(2);
    assertThat(allUsers.get(0).getStatus()).isNotNull();
    assertThat(allUsers.get(0).getProfile()).isNotNull();
  }
}
