package com.spirnt.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.spirnt.mission.discodeit.entity.User;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;


    @Test
    @DisplayName("findAllFetchJoin 메서드 테스트 - 성공")
    void testFindAllFetchJoinSuccess() {
        // when
        List<User> users = userRepository.findAllFetchJoin();

        // then
        assertNotNull(users);
        assertEquals(2, users.size());

        for (User user : users) {
            assertNotNull(user.getProfile());
            assertNotNull(user.getStatus());
            assertNotNull(user.getUsername());
            assertNotNull(user.getEmail());
        }
    }

    // 실패는 어떤 경우여야 할까?

}
