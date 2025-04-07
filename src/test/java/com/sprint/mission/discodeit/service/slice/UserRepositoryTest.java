package com.sprint.mission.discodeit.service.slice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaAuditing
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void save_Success() {
    User user = new User("username", "test@gmail.com", "qwer1234!", null);
    User result = userRepository.save(user);

    assertNotNull(result);
  }

  @Test
  void findByUsername_Success() {
    // given
    User user = new User("username", "test@gmail.com", "qwer1234!", null);
    userRepository.save(user);

    // when
    Optional<User> result = userRepository.findByUsername("username");

    // then
    assertTrue(result.isPresent());
    assertEquals("username", result.get().getUsername());
  }

  @Test
  void existsByEmail_Success() {
    // given
    User user = new User("username", "test@gmail.com", "qwer1234!", null);
    userRepository.save(user);

    // when
    boolean exists = userRepository.existsByEmail("test@gmail.com");

    // then
    assertTrue(exists);
  }

  @Test
  void existsByUsername_Success() {
    // given
    User user = new User("username", "test@gmail.com", "qwer1234!", null);
    userRepository.save(user);

    // when
    boolean exists = userRepository.existsByUsername("username");

    // then
    assertTrue(exists);
  }

  @Test
  void findAllWithProfileAndStatus_Success() {
    // given
    User user1 = new User("username1", "test1@gmail.com", "qwer1234!",
        new BinaryContent("name", 2L, "text"));
    UserStatus userStatus1 = new UserStatus(user1, Instant.now());
    User user2 = new User("username2", "test2@gmail.com", "qwer1234!",
        new BinaryContent("name", 2L, "text"));
    UserStatus userStatus2 = new UserStatus(user2, Instant.now());

    userRepository.save(user1);
    userRepository.save(user2);

    // when
    List<User> users = userRepository.findAllWithProfileAndStatus();

    // then
    assertEquals(2, users.size());
    assertTrue(users.get(0).getUsername().equals("username1") || users.get(1).getUsername()
        .equals("username1"));
    assertTrue(users.get(0).getUsername().equals("username2") || users.get(1).getUsername()
        .equals("username2"));
  }
}
