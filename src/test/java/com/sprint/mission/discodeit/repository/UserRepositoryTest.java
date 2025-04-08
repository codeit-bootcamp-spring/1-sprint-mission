package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.config.QuerydslConfig;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.logging.LogConfiguration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@Import({LogConfiguration.class, QuerydslConfig.class})
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Test
  @DisplayName("status, profile fetch join한 유저 리스트 - 성공")
  void findUsers() throws Exception {
    List<User> users = userRepository.findUsers();

    assertNotNull(users);
    assertEquals(3, users.size());
    assertEquals("user1", users.get(0).getUsername());
  }

  @Test
  @DisplayName("status, profile fetch join한 유저 리스트 - 빈 리스트")
  void findUsersFail() throws Exception {
    String userId1 = "3a2c1f0d-6b9e-4e8a-a7c5-d4f2e9b8c1a0";
    String userId2 = "4b3d2e1f-7c0a-5f9b-b8d6-e5c3f2d1e0b9";
    String userId3 = "5c4e3f2d-8d1b-6a0c-c9e7-f6d4e2c0b9a8";
    userRepository.removeUserById(UUID.fromString(userId1));
    userRepository.removeUserById(UUID.fromString(userId2));
    userRepository.removeUserById(UUID.fromString(userId3));

    List<User> users = userRepository.findUsers();

    assertTrue(users.isEmpty());
  }

  @Test
  @DisplayName("유저 이름으로 유저 찾기")
  void findUserByUsername() throws Exception {
    User user1 = userRepository.findUserByUsername("user1");

    assertNotNull(user1);
    assertEquals("user1", user1.getUsername());
  }

  @Test
  @DisplayName("유저 이름으로 찾기 - 실패")
  void findUserByUsernameFail() throws Exception {
    User lee = userRepository.findUserByUsername("lee");

    assertNull(lee);
  }
}