package com.sprint.mission.discodeit.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UsernameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "user123",         // ✅ 정상적인 영어+숫자
            "john_doe",        // ✅ 밑줄 포함 허용
            "player.99",       // ✅ 마침표 포함 허용
            "nickname_",       // ✅ 밑줄(_) 끝 가능
            "_nickname",       // ✅ 밑줄(_) 시작 가능
            "nickname.valid",  // ✅ 밑줄 + 마침표 포함 허용
            "validUser123",    // ✅ 대문자 포함 가능
            "user.name.123",   // ✅ 여러 개의 마침표 허용 (연속된 마침표 제외)
            "abcdefghijklmnopqrstuvwxyzabcd", // ✅ 32자 최대 허용
    })
    void 유저_이름_생성_성공(String username) {
        //given
        // when
        Username createUsername = new Username(username);
        // then
        assertThat(createUsername.getValue()).isEqualTo(username);
    }

}