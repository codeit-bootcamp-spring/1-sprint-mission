package com.sprint.mission.discodeit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserCreateTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  //@Transactional
  public void userCreateTest() {
    // Given: 새 사용자 생성
    User user_profile_null = new User("testuser", "testuser@example.com", "password123!", null);

    // When: 사용자 저장
    User savedUser = userRepository.save(user_profile_null);

    // Then: 저장된 사용자 확인
    assertThat(savedUser).isNotNull();
    assertThat(savedUser.getId()).isNotNull();
    assertThat(savedUser.getUsername()).isEqualTo("testuser");
    assertThat(savedUser.getEmail()).isEqualTo("testuser@example.com");
  }
}
