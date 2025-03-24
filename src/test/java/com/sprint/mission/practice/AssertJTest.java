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

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("유저 2").contains("유");
   }
}
