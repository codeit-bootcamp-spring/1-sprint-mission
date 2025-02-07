package com.sprint.mission.discodeit.repository.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sprint.mission.discodeit.entity.user.entity.User;
import com.sprint.mission.discodeit.repository.jcf.user.UserRepository;
import com.sprint.mission.discodeit.testdummy.TestDummyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRepositoryImplTest {
    private static final String TEST_NAME = "Test";
    private User user;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = TestDummyFactory.getUserRepository();
        user = User.createFrom(TEST_NAME);
        userRepository.save(user);
    }


    @Test
    @DisplayName("사용자 이름으로 찾을 때 유저정보 객체를 리턴")
    void givenUsernameWhenFindByNameThenReturnUser() {
        // given
        // when
        var findUser = userRepository.findByUsername(TEST_NAME).orElse(null);
        // then
        assertAll(
                () -> assertThat(findUser).isEqualTo(user),
                () -> assertThat(findUser).isNotNull()
        );
    }

}