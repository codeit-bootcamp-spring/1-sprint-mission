package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

class BasicUserServiceWithJCFTest {

    UserRepository userRepository = new JCFUserRepository();
    ChannelRepository channelRepository = new JCFChannelRepository();

    UserService userService = new BasicUserService(userRepository, channelRepository);

    @BeforeEach
    void init() {
        userService = new BasicUserService(userRepository, channelRepository);
    }

    @Test
    @DisplayName("유저 생성")
    void create() {
        User user = userService.createUser("user1", "user1@test.com", "password1234");

        Assertions.assertEquals("user1", userService.getUserById(user.getUserId()).getUserName());
        Assertions.assertEquals("user1@test.com", userService.getUserById(user.getUserId()).getUserEmail());
        Assertions.assertEquals("password1234", userService.getUserById(user.getUserId()).getUserPassword());

    }

    @Test
    @DisplayName("유저 정보 가져오기")
    void getUser() {
        User user1 = userService.createUser("user1", "user1@test.com", "password1234");
        User user2 = userService.createUser("user2", "user2@test.com", "22password1234");

        User findUser = userService.getUserById(user2.getUserId());

        Assertions.assertEquals(user2, findUser);
    }

    @Test
    @DisplayName("모든 유저 정보 가져오기")
    void getUserALL() {
        User user1 = userService.createUser("user1", "user1@test.com", "password1234");
        User user2 = userService.createUser("user2", "user2@test.com", "22password1234");

        Map<UUID, User> allUsers = userService.getAllUsers();

        Assertions.assertEquals(user1, allUsers.get(user1.getUserId()));
        Assertions.assertEquals(user2, allUsers.get(user2.getUserId()));
    }

    @Test
    @DisplayName("유저 업데이트")
    void update() {
        User user1 = userService.createUser("user1", "user1@test.com", "password1234");
        User user2 = userService.createUser("user2", "user2@test.com", "22password1234");

        User updateUser = userService.updateUser(user1.getUserId(), "newName", "newEmail@gmail.com", "newPassword123");

        Assertions.assertEquals("newName", updateUser.getUserName());
        Assertions.assertEquals("newEmail@gmail.com", updateUser.getUserEmail());
        Assertions.assertEquals("newPassword123", updateUser.getUserPassword());
    }

    @Test
    @DisplayName("유저 삭제")
    void deleteUser() {
        User user1 = userService.createUser("user1", "user1@test.com", "password1234");
        User user2 = userService.createUser("user2", "user2@test.com", "22password1234");

        userService.deleteUser(user1.getUserId());

        Assertions.assertEquals(null, userService.getUserById(user1.getUserId()));
    }
}