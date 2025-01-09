package com.sprint.mission.discodeit.db.user;

import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.db.user.ifs.UserRepository;
import com.sprint.mission.discodeit.testdummy.TestDummyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRepositoryImplTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = TestDummyFactory.getUserRepository();
    }


    @Test
    void givenUsernameWhenFindByNameThenReturnUser() {

    }
}