package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.jpa.user.UserJpaRepository;
import com.sprint.mission.discodeit.repository.jpa.user.UserRepositoryImpl;
import com.sprint.mission.discodeit.repository.jpa.userStatus.UserStatusRepositoryImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import({UserRepositoryImpl.class, UserStatusRepositoryImpl.class})
public class UserRepositoryTest {

  @Autowired
  UserJpaRepository userJpaRepository;

  @Autowired
  UserRepositoryImpl userRepository;

  @Autowired
  UserStatusRepositoryImpl userStatusRepository;

  private UUID saveUser(int num) {
    User user = User.of("name" + num, "email" + num,
        "password" + num);
    return userRepository.saveUser(user).getId();
  }

  @Nested
  class SaveUserTest {

    @Test
    void 유저_저장_성공() {
      // given
      User user = User.of("name1", "email", "password1");

      // when
      User savedUser = userRepository.saveUser(user);

      // then
      assertNotNull(savedUser);
      assertEquals("name1", savedUser.getName());
      assertEquals("email", savedUser.getEmail());
      assertEquals("password1", savedUser.getPassword());
    }
  }

  @Nested
  class FindUserByIdTest {

    @Test
    void 유저_아이디로_조회_성공() {
      // given
      int num = 1;
      UUID userId = saveUser(num);

      // when
      Optional<User> userOptional = userRepository.findUserById(userId);

      // then
      assertTrue(userOptional.isPresent());

      User user = userOptional.get();
      assertEquals("name" + num, user.getName());
    }

    @Test
    void 존재하지_않는_유저_아이디로_조회_() {
      // given
      UUID userId = UUID.randomUUID();

      // when
      Optional<User> userOptional = userRepository.findUserById(userId);

      // then
      assertFalse(userOptional.isPresent());
    }
  }

  @Nested
  class FindAllUsersTest {

    @Test
    void 유저_목록_조회_성공() {
      // given
      int size = 10;
      saveUsers(size);

      // when
      List<User> userList = userRepository.findAllUsers();

      // then
      assertEquals(size, userList.size());
    }

    private void saveUsers(int size) {
      for (int i = 0; i < size; i++) { // userStatus와 fetch join이기 때문에 추가적으로 같이 저장
        User user = User.of("name" + i, "email" + i,
            "password" + i);
        userStatusRepository.saveUserStatus(UserStatus.of(userRepository.saveUser(user)));
      }
    }
  }

  @Nested
  class RemoveUserTest {

    @Test
    void 유저_삭제_성공() {
      // given
      UUID userId = saveUser(0);

      // when
      userRepository.removeUser(userId);

      // then
      Optional<User> userOptional = userRepository.findUserById(userId);
      assertTrue(userOptional.isEmpty());
    }

    @Test
    void 존재하지_않는_유저_아이디로_삭제() {
      // given
      UUID userId = saveUser(0);

      // when
      userRepository.removeUser(UUID.randomUUID());

      // then
      Optional<User> userOptional = userRepository.findUserById(userId);
      assertTrue(userOptional.isPresent());
    }
  }

  @Nested
  class ExistsUserTest {

    @Test
    void 유저_존재_여부_확인_성공() {
      // given
      UUID userId = saveUser(0);

      // when
      boolean isExist = userRepository.existsUser(userId);

      // then
      assertTrue(isExist);
    }

    @Test
    void 유저_존재_여부_확인_실패() {
      // given
      UUID userId = saveUser(0);

      // when
      boolean isExist = userRepository.existsUser(UUID.randomUUID());

      // then
      assertFalse(isExist);
    }
  }

  @Nested
  class FindUserByNameTest {

    @Test
    void Name으로_유저_조회_성공() {
      // given
      int num = 0;
      saveUser(num);

      // when
      Optional<User> userOptional = userRepository.findUserByName("name" + num);

      // then
      assertTrue(userOptional.isPresent());

      User user = userOptional.get();
      assertEquals("name" + num, user.getName());
    }

    @Test
    void Name으로_유저_조회_실패() {
      // given
      int num = 0;
      saveUser(num);

      // when
      Optional<User> userOptional = userRepository.findUserByName("name" + 1);

      // then
      assertTrue(userOptional.isEmpty());
    }
  }

  @Nested
  class FindUserByEmailTest {

    @Test
    void Email로_유저_조회_성공() {
      // given
      saveUser(0);

      // when
      Optional<User> userOptional = userRepository.findUserByEmail("email" + 0);

      // then
      assertTrue(userOptional.isPresent());

      User user = userOptional.get();
      assertEquals("email" + 0, user.getEmail());
    }

    @Test
    void Email로_유저_조회_실패() {
      // given
      saveUser(0);

      // when
      Optional<User> userOptional = userRepository.findUserByEmail("email" + 1);

      // then
      assertTrue(userOptional.isEmpty());
    }
  }

  @Nested
  class FindUserByNameAndPasswordTest {

    @Test
    void Name_Password로_유저_조회_성공() {
      // given
      saveUser(0);

      // when
      Optional<User> userOptional =
          userRepository.findUserByNameAndPassword("name" + 0, "password" + 0);

      // then
      assertTrue(userOptional.isPresent());

      User user = userOptional.get();
      assertEquals("name" + 0, user.getName());
      assertEquals("password" + 0, user.getPassword());
    }

    @Test
    void Name_Password로_유저_조회_실패() {
      // given
      saveUser(0);

      // when
      Optional<User> userOptional =
          userRepository.findUserByNameAndPassword(null, "password" + 0);

      // then
      assertTrue(userOptional.isEmpty());
    }
  }
}
