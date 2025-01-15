package com.sprint.mission.discodeit.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("유일", "john.doe@example.com", "asdzxc7894", "Password123!");
    }

    @Test
    @DisplayName("유저의 이름이 올바르게 설정되었는지 확인")
    void testUserName() {
        assertEquals("유일", user.getName(), "User name should be '유일'");
    }

    @Test
    @DisplayName("유저의 이메일이 올바르게 설정되었는지 확인")
    void testUserEmail() {
        assertEquals("john.doe@example.com", user.getEmail(), "User email should be 'john.doe@example.com'");
    }

    @Test
    @DisplayName("유저의 비밀번호가 올바르게 설정되었는지 확인")
    void testUserPassword() {
        assertEquals("Password123!", user.getPassword(), "User password should be 'Password123!'");
    }

    @Test
    @DisplayName("유저의 이름 길이가 최소 조건을 만족하는지 확인")
    void testUserNameLength() {
        assertTrue(user.getName().length() >= 2, "User name length should be at least 2 characters");
    }
}
