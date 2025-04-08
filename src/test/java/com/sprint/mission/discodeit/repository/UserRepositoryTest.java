package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @BeforeEach
  void setUp() {
  }

  @Test
  @DisplayName("유저 저장 후 findById() 조회 성공")
  void testSaveAndFindById_Success() {
    // given
    User user = new User("testUser", "test@example.com", "pass123", null);

    // when
    User savedUser = userRepository.save(user);
    Optional<User> found = userRepository.findById(savedUser.getId());

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("testUser");
    assertThat(found.get().getEmail()).isEqualTo("test@example.com");
  }

  @Test
  @DisplayName("존재하지 않는 ID로 findById() 조회 실패")
  void testFindById_Fail() {
    // given
    UUID randomId = UUID.randomUUID();

    // when
    Optional<User> found = userRepository.findById(randomId);

    // then
    assertThat(found).isEmpty();
  }

  @Test
  @DisplayName("existsByEmail 테스트 (이메일 존재 여부 확인)")
  void testExistsByEmail() {
    // given
    User user = new User("userA", "userA@example.com", "passA", null);
    userRepository.save(user);

    // when
    boolean existEmail = userRepository.existsByEmail("userA@example.com");
    boolean notExistEmail = userRepository.existsByEmail("noOne@example.com");

    // then
    assertThat(existEmail).isTrue();
    assertThat(notExistEmail).isFalse();
  }

  @Test
  @DisplayName("existsByUsername 테스트 (유저명 존재 여부 확인)")
  void testExistsByUsername() {
    // given
    User user = new User("userB", "userB@example.com", "passB", null);
    userRepository.save(user);

    // when
    boolean existUsername = userRepository.existsByUsername("userB");
    boolean notExistUsername = userRepository.existsByUsername("notExistUser");

    // then
    assertThat(existUsername).isTrue();
    assertThat(notExistUsername).isFalse();
  }

  @Test
  @DisplayName("findByUsername 성공 테스트")
  void testFindByUsername_Success() {
    // given
    User user = new User("userC", "userC@example.com", "passC", null);
    userRepository.save(user);

    // when
    Optional<User> found = userRepository.findByUsername("userC");

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("userC@example.com");
  }

  @Test
  @DisplayName("findByUsername 실패 테스트 (없는 유저명)")
  void testFindByUsername_Fail() {
    // given

    // when
    Optional<User> found = userRepository.findByUsername("unknownUser");

    // then
    assertThat(found).isEmpty();
  }

  @Test
  @DisplayName("findAllWithProfileAndStatus 성공 테스트")
  void testFindAllWithProfileAndStatus_Success() {
    // given
    // profile 이 있는 유저와 없는 유저 각각 저장
    BinaryContent profile = new BinaryContent("profile.png", 100L, "image/png");
    User user1 = new User("user1", "user1@example.com", "pass1", profile);
    User user2 = new User("user2", "user2@example.com", "pass2", null);

    user1 = userRepository.save(user1);
    user2 = userRepository.save(user2);

    UserStatus status1 = new UserStatus(user1, Instant.now());
    UserStatus status2 = new UserStatus(user2, Instant.now());
    userStatusRepository.save(status1);
    userStatusRepository.save(status2);

    // when
    List<User> allUsers = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(allUsers).hasSize(2);
    assertThat(allUsers)
        .extracting("username")
        .containsExactlyInAnyOrder("user1", "user2");
  }
}
