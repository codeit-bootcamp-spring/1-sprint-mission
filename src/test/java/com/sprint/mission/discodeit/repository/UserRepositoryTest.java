package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.JpaConfig;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import(JpaConfig.class)
@ExtendWith(SpringExtension.class) // Junit5 + Spring
@DataJpaTest // JPA 와 연관된 Bean 만 로딩
public class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Test
  @DisplayName("유저 이름 존재 여부 테스트 : 성공")
  void existsByUsername_Success() {
    /**given**/
    User user = User.builder()
        .username("testUser")
        .email("test@example.com")
        .password("password123")
        .build();
    user = userRepository.save(user);

    /**when & then**/
    assertThat(userRepository.existsByUsername("testUser")).isTrue(); // 존재하는 것을 확인
  }

  @Test
  @DisplayName("유저 이름 존재 여부 테스트 : 실패")
  void existsByUsername_Fail() {
    assertThat(userRepository.existsByUsername("nonExistUser")).isFalse();
  }

  @Test
  @DisplayName("이메일 존재 여부 테스트 : 성공")
  void existsByEmail_Success() {
    /**given**/
    User user = User.builder()
        .username("testUser")
        .email("test@example.com")
        .password("password123")
        .build();
    user = userRepository.save(user);

    /**when & then**/
    assertThat(userRepository.existsByEmail("test@example.com")).isTrue(); // 존재하는 것을 확인
  }

  @Test
  @DisplayName("이메일 존재 여부 테스트 : 실패")
  void existsByEmail_Fail() {
    assertThat(userRepository.existsByEmail("nonExistUser")).isFalse();
  }
}
