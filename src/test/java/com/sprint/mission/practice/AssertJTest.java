package com.sprint.mission.practice;

import com.sprint.mission.entity.main.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AssertJTest {

    //테스트 간단 예시
    //
    //- 생성 후 값들이 생성자에 넣었떤 값들이 제대로 들어갔는지

//    public User(String username, String password, String email, BinaryContent profile) {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.profile = profile;
//    }

    @Test
    @DisplayName("유저 생성 테스트 : 무슨 테스트인지 명시하기 좋은 애노테이션")
    public void usingAssertJ() {
        var user = new User("유저 2", "페스워드 3", "이메일 100", null);

        // when
        var username = user.getUsername();

        // then
        Assertions.assertThat(username)
                .isEqualTo("유저 2")
                .contains("유")
                .doesNotContain("무");
    }

    @Test
    @DisplayName("description의 유용성")
    void descriptionTest() {
        var user = new User("유저 2", "페스워드 3", "이메일 100", null);

        // 주석으로 이렇게 뭔 검증인지 설명을 넣어도 되지만 as를 쓰면 이렇게 가독성 좋게할 수 잇따.
        assertThat(user).as("유저가 null이 아니어야 한다. %s", User.class).isNotNull();
        // as는 말그대로 설명하는거라 없어도 문제 없다.
        // 체이닝 방식이긴한데 as는 assertThat 바로 직후에 와야한다.
    }

    @Test
    @DisplayName("예외가 터지는지 검증 ")
    void exceptionTest() {
        var user = new User("example 2", "페스워드 3", "이메일 100", null);
        // 이름에 example이 들어가면 예외가 터지게 설정
        assertThatThrownBy(() -> user.assetName())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이름에 example");

        //assertThatNoException().isThrownBy(() -> user.assetName());
    }
}
