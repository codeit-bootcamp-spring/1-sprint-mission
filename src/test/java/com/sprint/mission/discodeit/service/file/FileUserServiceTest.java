package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUserServiceTest {
    FileUserService userService = new FileUserService();

    @Test
    @DisplayName("유저 생성")
    void create() {
        User user = userService.createUser("userA", "dsadqwda@naver.com", "pawdawd111");

        assertEquals("userA", user.getUserName());
        assertEquals("dsadqwda@naver.com", user.getUserEmail());
        assertEquals("pawdawd111", user.getUserPassword());
    }

    @Test
    @DisplayName("유저 단일 조회")
    void find() {
        User user1 = userService.createUser("userA", "dsadqwda@naver.com", "pawdawd111");
        User user2 = userService.createUser("userB", "q11qqqqqa@naver.com", "111ddddawd111");

        User findUser = userService.getUserById(user1.getUserId());

        assertEquals(user1, findUser);
    }

    @Test
    @DisplayName("유저 전체 조회")
    void findAll() {
        User user1 = userService.createUser("userA", "dsadqwda@naver.com", "pawdawd111");
        User user2 = userService.createUser("userB", "q11qqqqqa@naver.com", "111ddddawd111");

        Map<UUID, User> allUsers = userService.getAllUsers();

        assertEquals(2, allUsers.size());
        assertEquals(user1, allUsers.get(user1.getUserId()));
        assertEquals(user2, allUsers.get(user2.getUserId()));
    }

    @Test
    @DisplayName("유저 수정")
    void update() {
        User user1 = userService.createUser("userA", "dsadqwda@naver.com", "pawdawd111");

        userService.updateUser(user1.getUserId(), "newUser", "newnew@mail.com", "newPassword");

        assertEquals("newUser", user1.getUserName());
        assertEquals("newnew@mail.com", user1.getUserEmail());
        assertEquals("newPassword", user1.getUserPassword());
    }

    @Test
    @DisplayName("유저 삭제")
    void delete() {
        User user1 = userService.createUser("userA", "dsadqwda@naver.com", "pawdawd111");

        userService.deleteUser(user1.getUserId());

        assertEquals(0, userService.getAllUsers().size());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(user1.getUserId()));
    }
}