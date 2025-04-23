package com.sprint.mission.unit.repository;

import com.sprint.mission.entity.UserStatus;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.UserStatusRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema-test.sql"})
public class UserStatusRepoTest {

  @Autowired
  private UserStatusRepository userStatusRepository;
  @Autowired
  private TestEntityManager tem;

  @DisplayName("save 성공")
  @Test
  void save() {
    // given
    User user = new User("테스트 유저", "비밀번호", "이메일", null);
    tem.persistAndFlush(user);
    UserStatus userStatus = new UserStatus(user);

    // when
    userStatusRepository.save(userStatus);
    tem.flush();
    UserStatus foundUserStatus = userStatusRepository.findById(userStatus.getId()).orElse(null);

    // then
    assertThat(foundUserStatus).isNotNull();
    assertThat(foundUserStatus.getId()).isEqualTo(userStatus.getId());
    assertThat(foundUserStatus.getUser()).isEqualTo(userStatus.getUser());
    assertThat(foundUserStatus).isEqualTo(userStatus);
  }

  @DisplayName("User로 조회 성공")
  @Test
  void findByUser() {
    // given
    User user = new User("테스트 유저", "비밀번호", "이메일", null);
    tem.persistAndFlush(user);

    UserStatus userStatus = new UserStatus(user);
    tem.persistAndFlush(userStatus);
    tem.clear();

    // when
    UserStatus foundUserStatus = userStatusRepository.findByUser(user).orElse(null);

    // then
    assertThat(foundUserStatus).isNotNull();
    assertThat(foundUserStatus).isEqualTo(userStatus);
  }

  @DisplayName("User로 조회 실패 : 잘못된 유저 매칭")
  @Test
  void findByUserFail() {
    // given
    User user = new User("테스트 유저", "비밀번호", "이메일", null);
    User fakeUser = new User("가짜 유저", "가짜 비밀번호", "가짜 이메일", null);
    tem.persistAndFlush(user);
    tem.persistAndFlush(fakeUser);

    UserStatus userStatus = new UserStatus(user);
    tem.persistAndFlush(userStatus);
    tem.clear();

    // when, then
    Optional<UserStatus> foundUser = userStatusRepository.findByUser(fakeUser);

    assertThat(foundUser).isEmpty();
  }

  @DisplayName("isExistByUser 성공")
  @Test
  void isExist() {
    // given
    User user = new User("테스트 유저", "비밀번호", "이메일", null);
    tem.persistAndFlush(user);
    UserStatus userStatus = new UserStatus(user);
    tem.persistAndFlush(userStatus);
    tem.clear();

    // when
    boolean isExist = userStatusRepository.existsByUser(user);

    // then
    assertThat(isExist).isTrue();
  }

  @DisplayName("isExistByUser 실패")
  @Test
  void isExistFail() {
    // given
    User user = new User("테스트 유저", "비밀번호", "이메일", null);
    User fakeUser = new User("가짜 유저", "가짜 비밀번호", "가짜 이메일", null);
    tem.persistAndFlush(user);
    tem.persistAndFlush(fakeUser);
    UserStatus userStatus = new UserStatus(user);
    tem.persistAndFlush(userStatus);
    tem.clear();

    // when
    boolean isExist = userStatusRepository.existsByUser(fakeUser);

    // then
    assertThat(isExist).isFalse();
  }
}
