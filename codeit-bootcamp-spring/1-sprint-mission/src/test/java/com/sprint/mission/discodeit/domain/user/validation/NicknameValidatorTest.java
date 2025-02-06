package com.sprint.mission.discodeit.domain.user.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.domain.user.Nickname;
import com.sprint.mission.discodeit.domain.user.exception.NickNameInvalidException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class NicknameValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("nicknameOverLengthProvider")
    @ValueSource(strings = {
            "admin",              // 금지된 단어
            "moderator",          // 금지된 단어
            "discord123",         // 금지된 단어 포함
            "superadmin",         // 금지된 단어 포함
            "mod🚀",             // 금지된 단어 포함 + 이모지 (이모지는 가능하지만 금지어 포함이라 제한)
            "hello admin!",       // 금지된 단어 포함
            "AdminUser",          // 금지된 단어 포함 (대소문자 무시)
            "DISCORD🔥",         // 금지된 단어 포함 (대소문자 무시)
            "root_user",          // 금지된 단어 포함 (관리자 권한 관련)
            "system_mod",         // 금지된 단어 포함
            "운영자",              // 한글 운영자 금지
            "테스트관리자",         // 한글 관리자 관련 단어 포함
            "봇",                  // 봇 계정 금지
    })
    void 유저닉네임_제한되는_입력값_에러검증throw(String nickname) {
        //given
        // when
        Throwable catchThrowable = catchThrowable(() -> new Nickname(nickname));
        // then
        assertThat(catchThrowable).isInstanceOf(NickNameInvalidException.class);
    }

    static Stream<String> nicknameOverLengthProvider() {
        return Stream.of("a".repeat(33));
    }
}