//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import com.sprint.mission.discodeit.service.UserService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.Map;
//import java.util.UUID;
//
//class BasicUserServiceWithJCFTest {
//
//    UserRepository userRepository = new JCFUserRepository();
//
//    UserService userService = new BasicUserService(userRepository);
//
//    @BeforeEach
//    void init() {
//        userService = new BasicUserService(userRepository);
//    }
//
//    @Test
//    @DisplayName("유저 생성")
//    void create() {
//        User user = userService.createUser("user1", "user1@test.com", "password1234");
//
//        Assertions.assertEquals("user1", userService.getUserById(user.getId()).getUserName());
//        Assertions.assertEquals("user1@test.com", userService.getUserById(user.getId()).getUserEmail());
//        Assertions.assertEquals("password1234", userService.getUserById(user.getId()).getUserPassword());
//
//    }
//
//    @Test
//    @DisplayName("유저 정보 가져오기")
//    void getUser() {
//        User user1 = userService.createUser("user1", "user1@test.com", "password1234");
//        User user2 = userService.createUser("user2", "user2@test.com", "22password1234");
//
//        User findUser = userService.getUserById(user2.getId());
//
//        Assertions.assertEquals(user2, findUser);
//    }
//
//    @Test
//    @DisplayName("모든 유저 정보 가져오기")
//    void getUserALL() {
//        User user1 = userService.createUser("user1", "user1@test.com", "password1234");
//        User user2 = userService.createUser("user2", "user2@test.com", "22password1234");
//
//        Map<UUID, User> allUsers = userService.getAllUsers();
//
//        Assertions.assertEquals(user1, allUsers.get(user1.getId()));
//        Assertions.assertEquals(user2, allUsers.get(user2.getId()));
//    }
//
//    @Test
//    @DisplayName("유저 업데이트")
//    void update() {
//        User user1 = userService.createUser("user1", "user1@test.com", "password1234");
//        User user2 = userService.createUser("user2", "user2@test.com", "22password1234");
//
//        User updateUser = userService.updateUser(user1.getId(), "newName", "newEmail@gmail.com", "newPassword123");
//
//        Assertions.assertEquals("newName", updateUser.getUserName());
//        Assertions.assertEquals("newEmail@gmail.com", updateUser.getUserEmail());
//        Assertions.assertEquals("newPassword123", updateUser.getUserPassword());
//    }
//
//    @Test
//    @DisplayName("유저 삭제")
//    void deleteUser() {
//        User user1 = userService.createUser("user1", "user1@test.com", "password1234");
//        User user2 = userService.createUser("user2", "user2@test.com", "22password1234");
//
//        userService.deleteUser(user1.getId());
//
//        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(user1.getId()));
//    }
//}