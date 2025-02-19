package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.serial.FileUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class FileUserRepositoryTest {

    UserRepository userRepository = new FileUserRepository();

    @BeforeEach
    void init() {
        userRepository = new FileUserRepository();
    }

    @Test
    @DisplayName("직렬화를 통한 유저 저장")
    void save() {
        User user = new User("user1", "user@test.com", "user1234");

        User saveUser = userRepository.userSave(user);

        Assertions.assertEquals(user, saveUser);
    }

    @Test
    @DisplayName("유저 단일 조회")
    void find() {
        User user1 = new User("user1", "user@test.com", "user1234");
        User user2 = new User("user2", "user2@test.com", "user1234");
        userRepository.userSave(user1);
        userRepository.userSave(user2);

        User findUser1 = userRepository.findUserById(user1.getId());
        User findUser2 = userRepository.findUserById(user2.getId());

        Assertions.assertEquals(user1, findUser1);
        Assertions.assertEquals(user2, findUser2);
    }

    @Test
    @DisplayName("역직렬화를 통한 모든 유저 조회")
    void findAll() {
        User user1 = new User("user1", "user@test.com", "user1234");
        User user2 = new User("user2", "user2@test.com", "user1234");
        Map<UUID, User> testMap = new HashMap<>();

        testMap.put(user1.getId(), user1);
        testMap.put(user2.getId(), user2);

        userRepository.userSave(user1);
        userRepository.userSave(user2);

        Map<UUID, User> allUser = userRepository.findAllUser();

        Assertions.assertEquals(testMap, allUser);
    }

    @Test
    @DisplayName("유저 삭제")
    void delete() {
        User user1 = new User("user1", "user@test.com", "user1234");
        User user2 = new User("user2", "user2@test.com", "user1234");
        userRepository.userSave(user1);
        userRepository.userSave(user2);

        userRepository.removeUserById(user1.getId());

        Assertions.assertEquals(null, userRepository.findUserById(user1.getId()));
    }

}