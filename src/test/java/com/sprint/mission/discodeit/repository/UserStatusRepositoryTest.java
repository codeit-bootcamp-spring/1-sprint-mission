package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class UserStatusRepositoryTest {

  @Autowired
  UserStatusRepository userStatusRepository;
  @Autowired
  UserRepository userRepository;

  @Autowired
  TestEntityManager em;

  @Test
  @DisplayName("findByUserId")
  void findByUserId() {
    User user = User.create("username", "email", "password");
    UserStatus userStatus = UserStatus.create(user);
    userRepository.save(user);

    em.flush();
    em.clear();

    UserStatus findStatus = userStatusRepository.findByUserId(user.getId()).get();

    assertThat(findStatus.getId()).isEqualTo(userStatus.getId());
    assertThat(findStatus.getUser().getId()).isEqualTo(user.getId());
  }

  @Test
  @DisplayName("findAllWithUser")
  void findAllWithUser() {
    User user = User.create("username", "email", "password");
    UserStatus userStatus = UserStatus.create(user);
    userRepository.save(user);

    em.flush();
    em.clear();

    List<UserStatus> userStatuses = userStatusRepository.findAllWithUser();
    UserStatus findStatus = userStatuses.get(0);

    assertThat(findStatus.getId()).isEqualTo(userStatus.getId());
    assertThat(findStatus.getUser().getId()).isEqualTo(user.getId());
  }


}