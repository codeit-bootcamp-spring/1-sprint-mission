package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EntityManager em;


  @Test
  void 유저_저장_및_조회() {
    User user = new User("홍길동", "hong@naver.com", "1234", null);
    user.setStatus(new UserStatus(user, Instant.now()));

    userRepository.save(user);
    em.flush();
    em.clear();

    Optional<User> result = userRepository.findByUsername("홍길동");

    assertTrue(result.isPresent());
    assertEquals(user.getUsername(), result.get().getUsername());
    assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
  }

  @Test
  void 이메일_존재_여부_테스트() {
    User user = new User("홍길동", "hong@naver.com", "1234", null);

    userRepository.save(user);
    em.flush();
    em.clear();

    boolean exists = userRepository.existsByEmail("hong@naver.com");
    assertThat(exists).isTrue();
  }

  @Test
  void 존재하지_않는_이메일_존재_여부_테스트() {
    boolean exists = userRepository.existsByEmail("없는이메일@naver.com");
    assertThat(exists).isFalse();
  }

  @Test
  void 이름_존재_여부_테스트() {
    User user = new User("홍길동", "hong@naver.com", "1234", null);

    userRepository.save(user);
    em.flush();
    em.clear();

    boolean exists = userRepository.existsByUsername("홍길동");
    assertThat(exists).isTrue();
  }

  @Test
  void 존재하지_않는_이름_존재_여부_테스트() {
    boolean exists = userRepository.existsByUsername("없는이름");
    assertThat(exists).isFalse();
  }

  @Test
  void 아이디_존재_여부_테스트() {
    User user = new User("홍길동", "hong@naver.com", "1234", null);

    userRepository.save(user);
    em.flush();
    em.clear();

    boolean exists = userRepository.existsById(user.getId());
    assertThat(exists).isTrue();
  }


  @Test
  void 존재하지_않는_아이디_존재_여부_테스트() {
    UUID randomId = UUID.randomUUID();
    boolean exists = userRepository.existsById(randomId);
    assertThat(exists).isFalse();
  }

  @Test
  void findAll() {
    User user1 = new User("홍길동", "hong@naver.com", "1234", null);
    User user2 = new User("김창우", "kim@naver.com", "1234", null);

    userRepository.save(user1);
    userRepository.save(user2);

    List<User> users = userRepository.findAll();

    assertThat(users).hasSize(2);
    assertThat(users.get(0).getStatus()).isNull();
  }

  @Test
  void findByUsername() {
    User user = new User("홍길동", "hong@naver.com", "1234", null);
    userRepository.save(user);

    Optional<User> result = userRepository.findByUsername("홍길동");

    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getUsername()).isEqualTo("홍길동");
  }

  @Test
  void 존재하지_않는_username_조회시_Optional이_비어있다() {
    Optional<User> result = userRepository.findByUsername("없는이름");
    assertThat(result).isEmpty();
  }
}