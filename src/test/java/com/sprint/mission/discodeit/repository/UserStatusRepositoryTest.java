package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.entity.*;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
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
public class UserStatusRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(UserStatusRepositoryTest.class);

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private BinaryContentRepository binaryContentRepository;

  private User createTestUserWithStatus(String username, String email) {
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
  @DisplayName("사용자 상태를 저장하고 ID로 조회할 수 있다")
  void saveAndFindUserStatusById() {
    log.info("======== 사용자 상태 저장 및 ID로 조회 테스트 시작 ========");

    User user = createTestUserWithStatus("statususer", "status@example.com");
    UserStatus savedStatus = user.getStatus();
    Optional<UserStatus> found = userStatusRepository.findById(savedStatus.getId());

    assertThat(found).isPresent();
    assertThat(found.get().getUser().getId()).isEqualTo(user.getId());
    assertThat(found.get().getLastActiveAt()).isNotNull();

    log.info("======== 사용자 상태 저장 및 ID로 조회 테스트 종료 ========");
  }

  @Test
  @Order(2)
  @DisplayName("사용자 ID로 상태를 조회할 수 있다")
  void findUserStatusByUserId() {
    log.info("======== 사용자 ID로 상태 조회 테스트 시작 ========");

    User user = createTestUserWithStatus("userbyid", "userbyid@example.com");
    Optional<UserStatus> found = userStatusRepository.findByUserId(user.getId());

    assertThat(found).isPresent();
    assertThat(found.get().getUser().getId()).isEqualTo(user.getId());

    log.info("======== 사용자 ID로 상태 조회 테스트 종료 ========");
  }

  @Test
  @Order(3)
  @DisplayName("존재하지 않는 사용자 ID로 조회 시 빈 결과를 반환한다")
  void findByInvalidUserIdReturnsEmpty() {
    log.info("======== 존재하지 않는 사용자 ID로 상태 조회 테스트 시작 ========");

    Optional<UserStatus> found = userStatusRepository.findByUserId(UUID.randomUUID());

    assertThat(found).isEmpty();

    log.info("======== 존재하지 않는 사용자 ID로 상태 조회 테스트 종료 ========");
  }

  @Test
  @Order(4)
  @DisplayName("사용자 상태를 갱신할 수 있다")
  void updateUserStatus() {
    log.info("======== 사용자 상태 업데이트 테스트 시작 ========");

    User user = createTestUserWithStatus("updateuser", "update@example.com");
    UserStatus status = user.getStatus();
    Instant originalTime = status.getLastActiveAt();
    Instant newTime = Instant.now().plusSeconds(3600);

    status.update(newTime);
    userStatusRepository.save(status);
    entityManager.flush();
    entityManager.clear();

    Optional<UserStatus> updated = userStatusRepository.findByUserId(user.getId());
    assertThat(updated).isPresent();
    assertThat(updated.get().getLastActiveAt()).isNotEqualTo(originalTime);
    assertThat(updated.get().getLastActiveAt()).isEqualTo(newTime);

    log.info("======== 사용자 상태 업데이트 테스트 종료 ========");
  }

  @Test
  @Order(5)
  @DisplayName("사용자 상태를 삭제할 수 있다")
  void deleteUserStatus() throws Exception {
    log.info("======== 사용자 상태 삭제 테스트 시작 ========");

    User user = createTestUserWithStatus("deletestatususer", "deletestatus@example.com");
    UserStatus status = user.getStatus();
    assertThat(userStatusRepository.findById(status.getId())).isPresent();

    java.lang.reflect.Field statusField = User.class.getDeclaredField("status");
    statusField.setAccessible(true);
    statusField.set(user, null);

    userStatusRepository.deleteById(status.getId());
    entityManager.flush();
    entityManager.clear();

    Optional<UserStatus> deleted = userStatusRepository.findById(status.getId());
    assertThat(deleted).isEmpty();

    log.info("======== 사용자 상태 삭제 테스트 종료 ========");
  }

  @Test
  @Order(6)
  @DisplayName("사용자 상태가 온라인임을 확인할 수 있다")
  void checkUserOnlineStatus() {
    log.info("======== 사용자 온라인 상태 테스트 시작 ========");

    User user = createTestUserWithStatus("onlinestatus", "online@example.com");
    UserStatus status = user.getStatus();

    assertThat(status.isOnline()).isTrue();

    log.info("======== 사용자 온라인 상태 테스트 종료 ========");
  }
}
