package com.sprint.mission.discodeit.entity.user.domain;

import static org.assertj.core.api.Assertions.*;

import com.sprint.mission.discodeit.domain.user.Nickname;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class NicknameTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_닉네임_생성_null값으로생성_에러throw(String value) {
        //given
        // when
        Throwable catchThrowable = catchThrowable(() -> new Nickname(value));
        // then
        assertThat(catchThrowable).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testMethodNameHere() {
        //given
        String nickname = "test";
        Nickname nickname1 = new Nickname(nickname);
        Nickname nickname2 = new Nickname(nickname);
        // when
        boolean result = nickname1.equals(nickname2);

        // then
        assertThat(result).isTrue();
    }

}