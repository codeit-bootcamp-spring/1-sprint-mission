package com.sprint.mission.discodeit.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class UserTest {
    private static final Logger log = LoggerFactory.getLogger(ChannelTest.class);
    private User user =  new User("유일", "john.doe@example.com", "asdzxc7894", "Password123!");


    @BeforeEach
    void setUp() {
        log.info("======================== testStart ===========================");
    }

    @AfterEach
    void testEnd() {
        log.info("======================== Test End ============================");
    }

    @Test
    @DisplayName("유저의 이름이 올바르게 설정되었는지 확인")
    void testUserName() {
        assertEquals("유일", user.getName(), "유저 이름이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("유저의 이메일이 올바르게 설정되었는지 확인")
    void testUserEmail() {
        assertEquals("john.doe@example.com", user.getEmail(), "이메일 형식에 어긋납니다.");
    }

    @Test
    @DisplayName("유저의 비밀번호가 올바르게 설정되었는지 확인")
    void testUserPassword() {
        assertEquals("Password123!", user.getPassword(), " 최소 8자, 대문자, 소문자, 숫자, 특수문자 포함");
    }

    @Test
    @DisplayName("유저의 이름 길이가 최소 조건을 만족하는지 확인")
    void testUserNameLength() {
        assertTrue(user.getName().length() >= 2, "이름의 길이를 확인");
    }
}
