//package com.sprint.mission.discodeit.entity;
//
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
//import com.sprint.mission.discodeit.service.UserService;
//import com.sprint.mission.discodeit.service.jcf.JCFUserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class UserTest {
//    UserService userService = new JCFUserService();
//
//    @BeforeEach
//    void init() {
//        userService = new JCFUserService();
//    }
//
//    @Test
//    @DisplayName("유저가 제대로 생성되었는지 확인")
//    void createUser() {
//        User user = userService.createUser("test1", "test@test.com", "testpasswrod");
//
//        assertNotNull(user);
//        assertNotNull(user.getId());
//        assertEquals("test1", user.getUserName());
//        assertEquals("test@test.com", user.getUserEmail());
//        assertEquals("testpasswrod", user.getUserPassword());
//
//    }
//
//    @Test
//    @DisplayName("이름이 없을 때 검증")
//    void createNoneUserName() {
//        String name = "";
//        String email = "test@naver.com";
//        String pw = "1231234";
//
//        assertThrows(UserNotFoundException.class, () -> userService.createUser(name, email, pw));
//    }
//
//    @Test
//    @DisplayName("이메일이 없거나 잘못된 형식 검증")
//    void checkUserEmailNoneOrWrong() {
//        String name = "name";
//        String email1 = "testnaver.com";
//        String email2 = "test@navercom";
//        String pw = "1231234";
//
//        assertThrows(UserNotFoundException.class, () -> userService.createUser(name, email1, pw));
//        assertThrows(UserNotFoundException.class, () -> userService.createUser(name, email2, pw));
//    }
//
//    @Test
//    @DisplayName("비밀번호가 없거나 길이가 6보다 작을 경우 검증")
//    void checkUserPasswordNoneOrShort() {
//        String name = "name";
//        String email = "test@naver.com";
//        String pw1 = "";
//        String pw2 = "123";
//
//        assertThrows(UserNotFoundException.class, () -> userService.createUser(name, email, pw1));
//        assertThrows(UserNotFoundException.class, () -> userService.createUser(name, email, pw2));
//    }
//}