package com.sprint.mission.discodeit.repository.jpa;


import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.helper.UserTestFactory;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;


@EnableJpaAuditing
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EntityManager em;

  @Test
  @DisplayName("이메일로 유저가 존재하는 경우 true를 반환한다.")
  void existsByEmail() {
    //given
    User user = UserTestFactory.create("user1", "user1@test.com", "pw");
    userRepository.save(user);

    em.flush();
    em.clear();

    //when
    boolean exists = userRepository.existsByEmail("user1@test.com");

    //then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("존재하지 않는 이메일로 조회하면 false를 반환한다.")
  void existsByEmail_false() {
    //when then
    assertThat(userRepository.existsByEmail("none@test.com")).isFalse();
  }

  @Test
  @DisplayName("유저이름으로 유저가 존재하는지 확인할 수 있다.")
  void existsByUsername() {
    //given
    User user = UserTestFactory.create("myuser", "myuser@test.com", "pw");
    userRepository.save(user);

    //when
    boolean exists = userRepository.existsByUsername("myuser");

    //then
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("존재하지 않는 이름으로 조회하면 false를 반환한다.")
  void existsByUsername_false() {
    //when then
    assertThat(userRepository.existsByUsername("unknown")).isFalse();
  }

  @Test
  @DisplayName("유저 id로 userStatus, profile 를  함께 조회할 수 있다.")
  void findById_withEntityGraph() {
    //given
    User user = UserTestFactory.create("tester", "tester@test.com", "pw");
    UserStatus status = new UserStatus(user, Instant.now());
    user.addUserStatus(status);
    userRepository.save(user);

    em.flush();
    em.clear();

    //when
    Optional<User> result = userRepository.findById(user.getId());

    //then
    assertThat(result).isPresent();
    User found = result.get();
    assertThat(found.getStatus()).isNotNull();
    assertThat(found.getProfile()).isNull(); // 프로필은 넣지 않았으므로 null 확인
  }

  @Test
  @DisplayName("존재하지 않는 ID로 조회하면 Optional.empty()를 반환한다.")
  void findById_notExists() {
    //when then
    Optional<User> result = userRepository.findById(UUID.randomUUID());
    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("유저 목록 조회 시 연관 필드(userStatus, profile)도 함께 조회된다.")
  void findAll_shouldReturnUsersWithRelations() {
    //given
    User user1 = UserTestFactory.create("a", "a@test.com", "pw");
    User user2 = UserTestFactory.create("b", "b@test.com", "pw");
    UserStatus status1 = new UserStatus(user1, Instant.now());
    user1.addUserStatus(status1);

    userRepository.saveAll(List.of(user1, user2));

    em.flush();
    em.clear();

    //when
    List<User> result = userRepository.findAll();

    //then
    assertThat(result).hasSize(2);
    User found = result.get(0);
    assertThat(found.getStatus()).isNotNull();
  }

  @Test
  @DisplayName("유저가 존재하지 않으면 findAll()은 빈 리스트를 반환한다.")
  void findAll_whenEmpty_shouldReturnEmptyList() {
    //when then
    List<User> result = userRepository.findAll();
    assertThat(result).isEmpty();
  }
}