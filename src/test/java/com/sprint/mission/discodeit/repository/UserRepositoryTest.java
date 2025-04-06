package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  private User testUser;

  @BeforeEach
  void setUp() {
    // 테스트 데이터 생성
    testUser = User.builder()
        .name("testUser")
        .email("test@test.com")
        .password("1234")
        .online(false)
        .profileImage(null)
        .build();

    entityManager.flush();
  }

  @Test
  @DisplayName("사용자 저장 테스트")
  void save_TestUser() {
    // When
    User savedUser = userRepository.save(testUser);
    entityManager.flush();
    entityManager.clear();

    // Then
    User foundUser = entityManager.find(User.class, savedUser.getId());
    assertThat(foundUser).isNotNull();
    assertThat(foundUser.getName()).isEqualTo("testUser");
    assertThat(foundUser.getEmail()).isEqualTo("test@test.com");
    assertThat(foundUser.getPassword()).isEqualTo("1234");
    assertThat(foundUser.getProfileImage()).isNull();
    assertThat(foundUser.isOnline()).isFalse();
  }

  @Test
  @DisplayName("사용자 ID로 조회 테스트")
  void findById_Success() {
    // Given
    User savedUser = entityManager.persistAndFlush(testUser);
    entityManager.clear();

    // When
    Optional<User> foundUser = userRepository.findById(savedUser.getId());

    // Then
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());

  }

  @Test
  @DisplayName("사용자 전체 조회")
  void findAll_Success() {
    // Given
    entityManager.persistAndFlush(testUser);
    entityManager.clear();

    // When
    List<User> users = userRepository.findAll();

    // Then
    assertThat(users).isNotEmpty();
    assertThat(users).hasSize(1);
    assertThat(users.get(0).getName()).isEqualTo("testUser");

  }

  @Test
  @DisplayName("사용자 업데이트 테스트")
  void update_Success() {
    // Given
    User savedUser = entityManager.persistAndFlush(testUser);

    // When
    savedUser.setName("updatedUser");
    savedUser.setEmail("update@test.com");
    userRepository.save(savedUser);
    entityManager.flush();
    entityManager.clear();

    // Then
    User updatedUser = entityManager.find(User.class, savedUser.getId());
    assertThat(updatedUser).isNotNull();
    assertThat(updatedUser.getName()).isEqualTo("updatedUser");
    assertThat(updatedUser.getEmail()).isEqualTo("update@test.com");
  }

  @Test
  @DisplayName("사용자 삭제 테스트")
  void deleteById_Success() {
    // Given
    User savedUser = entityManager.persistAndFlush(testUser);
    entityManager.clear();

    // When
    userRepository.deleteById(savedUser.getId());
    entityManager.flush();
    entityManager.clear();

    // Then
    User deletedUser = entityManager.find(User.class, savedUser.getId());
    assertThat(deletedUser).isNull();

  }


}
