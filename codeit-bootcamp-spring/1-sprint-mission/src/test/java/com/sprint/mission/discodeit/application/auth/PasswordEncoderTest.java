package com.sprint.mission.discodeit.application.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordEncoderTest {

    @Test
    void 암호화_한_비밀번호_검증() {
        // given
        String password = "testPassword";
        PasswordEncoder encoder = new PasswordEncoder();
        // when
        String encodedPassword = encoder.encode(password);
        // then
        assertTrue(encoder.matches(password, encodedPassword));
        assertFalse(encoder.matches("anotherPassword",encodedPassword));
    }
}
