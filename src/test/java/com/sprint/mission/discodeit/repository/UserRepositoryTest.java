package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.entity.*;
import java.time.Instant;
import java.util.*;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestAuditingConfig.class)
public class UserRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;

  private User createTestUser(String username, String email) {
    BinaryContent profile = new BinaryContent(username + "-profile.jpg", 1024L, "image/jpeg");
    binaryContentRepository.save(profile);
    User user = new User(username, email, "password123!@#", profile);
    userRepository.save(user);
    userStatusRepository.save(new UserStatus(user, Instant.now()));
    entityManager.flush();
    return user;
  }

  @Test
  @Order(1)
  @DisplayName("사용자를 저장하고 ID로 조회할 수 있다")
  void saveAndFindUserById() {
    log.info("======== 사용자 저장 및 ID로 조회 테스트 시작 ========");

    String username = "testuser";
    String email = "test@example.com";
    User savedUser = createTestUser(username, email);

    Optional<User> foundUser = userRepository.findById(savedUser.getId());

    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getUsername()).isEqualTo(username);
    assertThat(foundUser.get().getEmail()).isEqualTo(email);

    log.info("======== 사용자 저장 및 ID로 조회 테스트 종료 ========");
  }

  @Test
  @Order(2)
  @DisplayName("사용자명을 기준으로 사용자 정보를 조회할 수 있다")
  void findUserByUsername() {
    log.info("======== 사용자명으로 조회 테스트 시작 ========");

    String username = "usernametest";
    String email = "username@example.com";
    createTestUser(username, email);

    Optional<User> foundUser = userRepository.findByUsername(username);

    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getUsername()).isEqualTo(username);
    assertThat(foundUser.get().getEmail()).isEqualTo(email);

    log.info("======== 사용자명으로 조회 테스트 종료 ========");
  }

  @Test
  @Order(3)
  @DisplayName("존재하지 않는 사용자명을 조회하면 빈 결과를 반환한다")
  void findByInvalidUsernameReturnsEmpty() {
    log.info("======== 존재하지 않는 사용자명으로 조회 테스트 시작 ========");

    Optional<User> foundUser = userRepository.findByUsername("nonexistentuser");

    assertThat(foundUser).isEmpty();

    log.info("======== 존재하지 않는 사용자명으로 조회 테스트 종료 ========");
  }

  @Test
  @Order(4)
  @DisplayName("이메일 존재 여부를 확인할 수 있다")
  void checkEmailExists() {
    log.info("======== 이메일 존재 여부 확인 테스트 시작 ========");

    String username = "emailtest";
    String email = "exists@example.com";
    createTestUser(username, email);

    boolean exists = userRepository.existsByEmail(email);

    assertThat(exists).isTrue();

    log.info("======== 이메일 존재 여부 확인 테스트 종료 ========");
  }

  @Test
  @Order(5)
  @DisplayName("존재하지 않는 이메일에 대해 false를 반환한다")
  void checkNonexistentEmailReturnsFalse() {
    log.info("======== 존재하지 않는 이메일 확인 테스트 시작 ========");

    boolean exists = userRepository.existsByEmail("nonexistent@example.com");

    assertThat(exists).isFalse();

    log.info("======== 존재하지 않는 이메일 확인 테스트 종료 ========");
  }

  @Test
  @Order(6)
  @DisplayName("사용자명 존재 여부를 확인할 수 있다")
  void checkUsernameExists() {
    log.info("======== 사용자명 존재 여부 확인 테스트 시작 ========");

    String username = "existsusername";
    String email = "existsusername@example.com";
    createTestUser(username, email);

    boolean exists = userRepository.existsByUsername(username);

    assertThat(exists).isTrue();

    log.info("======== 사용자명 존재 여부 확인 테스트 종료 ========");
  }

  @Test
  @Order(7)
  @DisplayName("프로필과 상태가 포함된 모든 사용자를 조회할 수 있다")
  void findAllUsersWithProfileAndStatus() {
    log.info("======== 프로필과 상태가 포함된 모든 사용자 조회 테스트 시작 ========");

    createTestUser("user1", "user1@example.com");
    createTestUser("user2", "user2@example.com");

    List<User> users = userRepository.findAllWithProfileAndStatus();

    assertThat(users).isNotEmpty();
    assertThat(users).extracting("username").contains("user1", "user2");
    assertThat(users.get(0).getStatus()).isNotNull();
    assertThat(users.get(1).getStatus()).isNotNull();

    log.info("======== 프로필과 상태가 포함된 모든 사용자 조회 테스트 종료 ========");
  }
}
