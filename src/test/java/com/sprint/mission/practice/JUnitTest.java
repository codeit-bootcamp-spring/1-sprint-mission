package com.sprint.mission.practice;

import com.sprint.mission.entity.main.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class JUnitTest {

    @Test
    @DisplayName("유저 생성 테스트 : 무슨 테스트인지 명시하기 좋은 애노테이션")
    public void usingAssertJ() {
        var user = new User("유저 2", "페스워드 3", "이메일 100", null);

        // when
        var username = user.getUsername();

        // then
        Assertions.assertEquals(username, "유저 2");
        Assertions.assertTrue(username.contains("유"));
        Assertions.assertFalse(username.contains("무"));
//        assertThat(user).isNotNull();
//        assertThat(user).isInstanceOf(User.class);
//        assertThat(user.getUsername()).isEqualTo("유저 2").contains("유");
    }
}
