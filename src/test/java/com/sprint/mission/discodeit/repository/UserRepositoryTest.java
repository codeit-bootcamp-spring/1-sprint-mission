package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Test
  @DisplayName("사용자 저장 - 성공")
  void saveUserSuccess() {
    User user = new User("이유빈", "yubin@codeit.com", "1234", null);
    User savedUser = userRepository.save(user);

    assertNotNull(savedUser.getId());
    assertEquals("이유빈", savedUser.getUsername());
    assertEquals("yubin@codeit.com", savedUser.getEmail());
  }

  @Test
  @DisplayName("사용자 저장 - 실패 (이메일 누락)")
  void saveUserFail() {
    User user = new User("이유빈", null, "1234", null);

    assertThrows(DataIntegrityViolationException.class, () -> {
      userRepository.saveAndFlush(user);
    });
  }

  @Test
  @DisplayName("사용자 모두 조회 - 성공")
  void findAllWithProfileAndStatusSuccess() {
    BinaryContent profile = binaryContentRepository.save(
        new BinaryContent("img001.jpeg", (long) "이미지".getBytes().length, "jpeg"));
    User user = new User("이유빈", "yubin@codeit.com", "1234", profile);
    userRepository.save(user);

    UserStatus status = userStatusRepository.save(
        new UserStatus(user, Instant.now()));
    userStatusRepository.save(status);

    List<User> users = userRepository.findAllWithProfileAndStatus();

    assertEquals(1, users.size());
    User result = users.get(0);
    assertNotNull(result.getProfile());
    assertNotNull(result.getStatus());
    assertEquals("img001.jpeg", result.getProfile().getFileName());
    assertEquals(status, result.getStatus());
  }

  @Test
  @DisplayName("사용자 모두 조회 - 실패 (상태는 있으나 프로필 없음)")
  void findAllWithProfileAndStatusFail() {
    User user = new User("이유빈", "yubin@codeit.com", "1234", null);
    userRepository.save(user);

    UserStatus status = new UserStatus(user, Instant.now());
    userStatusRepository.save(status);

    List<User> users = userRepository.findAllWithProfileAndStatus();

    assertNull(users.get(0).getProfile());
  }
}
