package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.JpaConfig;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import({JpaConfig.class})
@ActiveProfiles("test")
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  void saveUser_success() {
    // given
    User user = new User("u", "u@gmail.com", "pw", null, null);

    // when
    User savedUser = userRepository.save(user);
    em.flush();

    // then
    assertThat(savedUser.getId()).isNotNull();
  }

  @Test
  void saveUser_violates_usernameConstraint() {
    // given
    User user = new User(null, "u@gmail.com", "pw", null, null);

    //when & then
    Assertions.assertThatThrownBy(() -> {
          userRepository.save(user);
          em.flush();
        })
        .isInstanceOf(ConstraintViolationException.class);
  }

  @Test
  @DisplayName("findById 는 Profile 과 Status 를 함께 로드한다")
  void findById_withEntityGraph_loadsProfileAndStatus() {
    // given
    User user = persistUser("test");

    //when
    em.flush();
    em.clear();

    Optional<User> optionalUser = userRepository.findById(user.getId());

    // then
    assertThat(optionalUser).isPresent();
    User foundUser = optionalUser.get();

    assertThat(foundUser.getProfile()).isNotNull();
    assertThat(foundUser.getProfile().getFileName()).isEqualTo(user.getUsername() + ".png");
  }

  @Test
  @DisplayName("findById - 존재하지 않는 ID로 조회하면 빈 Optional이 반환된다")
  void findById_shouldReturnEmptyOptional_whenIdDoesNotExist() {
    // given
    UUID id = UUID.randomUUID();

    // when
    Optional<User> optionalUser = userRepository.findById(id);

    //then
    assertThat(optionalUser).isEmpty();
  }

  @Test
  @DisplayName("findAll - 저장된 유저들이 모두 조회된다")
  void findAll_withEntityGraph_loadsAssociations() {
    // given
    persistUser("user1");
    persistUser("user2");

    // when
    em.flush();
    em.clear();
    List<User> users = userRepository.findAll();

    // then
    assertThat(users).hasSize(2);
    users.forEach(u -> {
      assertThat(u.getProfile()).isNotNull();
    });
  }

  @Test
  @DisplayName("findAll - 저장된 user 가 없을 경우 빈 리스트가 반환된다")
  void findAll_shouldReturnEmptyList_whenNoUsersAreFound() {
    List<User> result = userRepository.findAll();
    assertThat(result).hasSize(0);
  }

  @Test
  @DisplayName("findAllByIdIn - 리스트에 포함된 User만 반환한다")
  void findAllByIdIn_success() {

    // given
    User u1 = persistUser("user1");
    User u2 = persistUser("user2");
    em.flush();
    em.clear();

    // when
    List<User> users = userRepository.findAllByIdIn(List.of(u1.getId()));

    // then
    assertThat(users).hasSize(1);
    assertThat(users).containsExactlyInAnyOrder(u1);
    users.forEach(u -> {
      assertThat(u.getProfile()).isNotNull();
    });
  }

  @Test
  @DisplayName("findByUsernameWithProfileAndStatus - 유저네임으로 조회 시 연관 객체도 함께 로딩된다")
  void findByUsernameWithProfileAndStatus_success() {
    persistUser("frank");
    em.flush();
    em.clear();

    Optional<User> found = userRepository.findByUsernameWithProfileAndStatus("frank");

    assertThat(found).isPresent();
    assertThat(found.get().getProfile().getFileName()).isEqualTo("frank.png");

  }

  @Test
  @DisplayName("findByUsernameWithProfileAndStatus - 존재하지 않는 username은 빈 Optional을 반환한다")
  void findByUsernameWithProfileAndStatus_fail() {
    Optional<User> result = userRepository.findByUsernameWithProfileAndStatus("nonexistent");
    assertThat(result).isEmpty();
  }

  private User persistUser(String username) {
    BinaryContent profile = new BinaryContent(username + ".png", 1024L, "image/png");
    User user = new User(username, username + "@test.com", "pw", profile, null);

    em.persist(profile);
    em.persist(user);

    return user;
  }
}
