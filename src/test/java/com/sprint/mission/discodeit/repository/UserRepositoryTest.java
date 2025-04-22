package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.common.Phone;
import com.sprint.mission.discodeit.config.TestAuditConfig;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("db")
@DataJpaTest()
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestAuditConfig.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager em;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = new User("test", "testuser", "test@email.com", "!@asdf4953",
                new Phone("010-1111-2222", Phone.RegionCode.KR), User.UserType.COMMON, "", null);
        em.persist(savedUser);

        UserStatus status = new UserStatus(Instant.now(), savedUser);
        em.persist(status);

        savedUser.updateUserStatus(status);
        em.flush();
    }

    @Test
    @DisplayName("사용자 명을 입력받아 사용자가 존재하는지 여부를 확인한다.")
    void existsUserByUsername() {
        boolean exists = userRepository.existsUserByUsername("test");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 이름을 입력하면 False를 반환한다.")
    void existsUserByUsernameFailed() {
        boolean exists = userRepository.existsUserByUsername("abcd");
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("사용자 조회 시 연관 관계의 userStatus와 binaryContent를 함께 반환한다.")
    void findByIdWithDetailsSuccess() {
        Optional<User> result = userRepository.findByIdWithDetails(savedUser.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getUserStatus()).isNotNull();
        assertThat(result.get().getProfile()).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회 시 빈 값을 반환한다.")
    void findByIdWithDetailsFailed() {
        Optional<User> result = userRepository.findByIdWithDetails(UUID.randomUUID());
        assertThat(result).isNotPresent();
    }

}