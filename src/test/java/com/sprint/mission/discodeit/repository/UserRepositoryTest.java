package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
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
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  TestEntityManager em;

  @Test
  @DisplayName("findByIdWithProfileAndStatus")
  void findByIdWithProfileAndStatus() {
    BinaryContent content = BinaryContent.create(1024L, "profile", "jpg");
    User user = User.create("username", "email", "password");
    UserStatus userStatus = UserStatus.create(user);
    user.updateProfile(content);
    userRepository.save(user);

    em.flush();
    em.clear();

    User findUser = userRepository.findByIdWithProfileAndStatus(user.getId()).get();

    BinaryContent findProfile = findUser.getProfile().orElseThrow();
    UserStatus findStatus = findUser.getStatus();
    assertThat(findStatus.getId()).isEqualTo(userStatus.getId());
    assertThat(findProfile.getId()).isEqualTo(content.getId());
  }

  @Test
  @DisplayName("findAllWithProfileAndStatus")
  void findAllWithProfileAndStatus() {
    BinaryContent content = BinaryContent.create(1024L, "profile", "jpg");

    User user1 = User.create("username1", "email1", "password1");
    UserStatus userStatus1 = UserStatus.create(user1);
    user1.updateProfile(content);
    userRepository.save(user1);

    User user2 = User.create("username2", "email2", "password2");
    UserStatus userStatus2 = UserStatus.create(user2);
    user2.updateProfile(content);
    userRepository.save(user2);

    em.flush();
    em.clear();

    List<User> findUsers = userRepository.findAllWithProfileAndStatus();
    User findUser1 = findUsers.get(0);
    User findUser2 = findUsers.get(1);

    assertThat(findUsers.size()).isEqualTo(2);
    assertThat(findUser1.getId()).isEqualTo(user1.getId());
    assertThat(findUser2.getId()).isEqualTo(user2.getId());
    assertThat(findUser1.getStatus().getId()).isEqualTo(user1.getStatus().getId());
  }
}