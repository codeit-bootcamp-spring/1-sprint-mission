package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

/**
 * UserStatusRepository 슬라이스 테스트
 */
@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class UserStatusRepositoryTest {

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  /**
   * TestFixture: 테스트용 사용자와 상태 생성
   */
  private User createTestUserWithStatus(String username, String email, Instant lastActiveAt) {
    BinaryContent profile = new BinaryContent("profile.jpg", 1024L, "image/jpeg");
    User user = new User(username, email, "password123!@#", profile);
    UserStatus status = new UserStatus(user, lastActiveAt);
    return userRepository.save(user);
  }

  @Test
  @DisplayName("사용자 ID로 상태 정보를 찾을 수 있다")
  void findByUserId_ExistingUserId_ReturnsUserStatus() {
    // given
    Instant now = Instant.now();
    User user = createTestUserWithStatus("testUser", "test@example.com", now);
    UUID userId = user.getId();

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    Optional<UserStatus> foundStatus = userStatusRepository.findByUserId(userId);

    // then
    assertThat(foundStatus).isPresent();
    assertThat(foundStatus.get().getUser().getId()).isEqualTo(userId);
    assertThat(foundStatus.get().getLastActiveAt()).isEqualTo(now);
  }

  @Test
  @DisplayName("존재하지 않는 사용자 ID로 검색하면 빈 Optional을 반환한다")
  void findByUserId_NonExistingUserId_ReturnsEmptyOptional() {
    // given
    UUID nonExistingUserId = UUID.randomUUID();

    // when
    Optional<UserStatus> foundStatus = userStatusRepository.findByUserId(nonExistingUserId);

    // then
    assertThat(foundStatus).isEmpty();
  }

  @Test
  @DisplayName("UserStatus의 isOnline 메서드는 최근 활동 시간이 5분 이내일 때 true를 반환한다")
  void isOnline_LastActiveWithinFiveMinutes_ReturnsTrue() {
    // given
    Instant now = Instant.now();
    User user = createTestUserWithStatus("testUser", "test@example.com", now);

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    Optional<UserStatus> foundStatus = userStatusRepository.findByUserId(user.getId());

    // then
    assertThat(foundStatus).isPresent();
    assertThat(foundStatus.get().isOnline()).isTrue();
  }

  @Test
  @DisplayName("UserStatus의 isOnline 메서드는 최근 활동 시간이 5분보다 이전일 때 false를 반환한다")
  void isOnline_LastActiveBeforeFiveMinutes_ReturnsFalse() {
    // given
    Instant sixMinutesAgo = Instant.now().minus(6, ChronoUnit.MINUTES);
    User user = createTestUserWithStatus("testUser", "test@example.com", sixMinutesAgo);

    // 영속성 컨텍스트 초기화
    entityManager.flush();
    entityManager.clear();

    // when
    Optional<UserStatus> foundStatus = userStatusRepository.findByUserId(user.getId());

    // then
    assertThat(foundStatus).isPresent();
    assertThat(foundStatus.get().isOnline()).isFalse();
  }
} 