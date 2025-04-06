package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일로 사용자 찾기 성공")
    void findByEmailSuccess() {
        // Given
        String email = "test@email.com";
        User user = new User("testuser", email, "password123!", null);
        entityManager.persist(user);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByEmail(email);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 사용자 찾기 실패")
    void findByEmailFail() {
        // Given
        String email = "nonexistent@email.com";

        // When
        Optional<User> found = userRepository.findByEmail(email);

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("사용자명으로 사용자 찾기 성공")
    void findByUsernameSuccess() {
        // Given
        String username = "testuser";
        User user = new User(username, "test@email.com", "password123!", null);
        entityManager.persist(user);
        entityManager.flush();

        // When
        Optional<User> found = userRepository.findByUsername(username);

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("페이징을 사용한 사용자 목록 조회")
    void findAllWithPaging() {
        // Given
        for (int i = 1; i <= 20; i++) {
            User user = new User(
                "user" + i,
                "user" + i + "@email.com",
                "password123!",
                null
            );
            entityManager.persist(user);
        }
        entityManager.flush();

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("username").ascending());

        // When
        Page<User> userPage = userRepository.findAll(pageRequest);

        // Then
        assertThat(userPage.getContent()).hasSize(10);
        assertThat(userPage.getTotalElements()).isEqualTo(20);
        assertThat(userPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("이메일 존재 여부 확인")
    void existsByEmailSuccess() {
        // Given
        String email = "test@email.com";
        User user = new User("testuser", email, "password123!", null);
        entityManager.persist(user);
        entityManager.flush();

        // When
        boolean exists = userRepository.existsByEmail(email);

        // Then
        assertThat(exists).isTrue();
    }
} 