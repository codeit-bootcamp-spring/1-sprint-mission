package com.sprint.mission.discodeit.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

public class TestUserStatus {

    @DisplayName("UserStatus - checkAccess() 테스트")
    @Test
    void userStatusTest() throws IOException {

        // given(준비): 어떠한 데이터가 준비되었을 때
        User user = new User("email@email.com", "PWpw12!!", "name", "nickname", "010-0000-1111", null);

        // when(실행) : 어떠한 함수를 실행하면
        boolean isUserAccess = user.getUserStatus().checkAccess();

        // then(검증) : 어떠한 결과가 나와야 한다.
        System.out.println(isUserAccess);
    }
}
