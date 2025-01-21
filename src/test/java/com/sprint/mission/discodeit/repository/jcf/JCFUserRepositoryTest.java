package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class JCFUserRepositoryTest {
    UserRepository userRepository = new JCFUserRepository();

    @BeforeEach
    void init() {
        userRepository = new JCFUserRepository();
    }
    @Test
    @DisplayName("유저 저장")
    void saveUser() {
        User user1 = new User("KIM", "dwqdwqdd@naver.com", "dwqdqdwd");
        User user2 = new User("KIM", "dwqdwqdd@naver.com", "dwqdqdwd");

        userRepository.userSave(user1);

        Assertions.assertEquals(user1, userRepository.findUserById(user1.getUserId()));
    }

    @Test
    @DisplayName("모든 유저 불러오기")
    void loadAllUser() {
        User user1 = userRepository.userSave(new User("kim", "dwqdwq@tss.com", "passwoooord"));
        User user2 = userRepository.userSave(new User("lee", "zkzkzk@nave.com", "pppppwwww"));
        Map<UUID, User> testMap = new HashMap<>();
        testMap.put(user1.getUserId(), user1);
        testMap.put(user2.getUserId(), user2);

        Map<UUID, User> userMap = userRepository.findAllUser();

        Assertions.assertEquals(testMap, userMap);
    }


    @Test
    @DisplayName("유저 제거")
    void delete() {
        User user1 = userRepository.userSave(new User("kim", "dwqdwq@tss.com", "passwoooord"));
        User user2 = userRepository.userSave(new User("lee", "zkzkzk@nave.com", "pppppwwww"));

        userRepository.removeUserById(user1.getUserId());

        Assertions.assertNull(userRepository.findUserById(user1.getUserId()));
    }
}