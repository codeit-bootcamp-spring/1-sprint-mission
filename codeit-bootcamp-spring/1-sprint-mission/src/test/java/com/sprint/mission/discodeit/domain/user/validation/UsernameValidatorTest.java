package com.sprint.mission.discodeit.domain.user.validation;

import com.sprint.mission.discodeit.domain.user.exception.UserNameInvalidException;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class UsernameValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "a",                      // 너무 짧음 (최소 2자)
            "abcdefghijklmnopqrstuvwxyzabcdefghi", // 너무 김 (32자 초과, 33자)
            "user name",              // 공백 포함 (허용되지 않음)
            "🔥coolgamer",           // 이모지 포함 (정규식이 허용하지 않음)
            "user!name",             // 특수문자 포함 (! 허용되지 않음)
            "....hidden....",         // 연속된 마침표 (허용되지 않음)
            ".username",              // 마침표로 시작 (허용되지 않음)
            "username.",              // 마침표로 끝남 (허용되지 않음)
            "admin",                  // 금지된 단어 포함
            "moderator",              // 금지된 단어 포함
            "discord123",             // 금지된 단어 포함
            "superadmin",             // 금지된 단어 포함
            "mod🚀",                  // 금지된 단어 포함 + 이모지
            "hello admin!",           // 금지된 단어 포함
            "AdminUser",              // 금지된 단어 포함 (대소문자 무시)
            "DISCORD🔥",             // 금지된 단어 포함 (대소문자 무시)
            "root_user",              // 금지된 단어 포함 (관리자 권한 관련)
            "system_mod",             // 금지된 단어 포함
            "운영자",                 // 한글 운영자 금지
            "테스트관리자",            // 한글 관리자 관련 단어 포함
            "봇",                     // 봇 계정 금지
    })
    @MethodSource("usernameOverLengthProvider")
    void testMethodNameHere(String username) {
        //given
        // when
        Throwable catchThrowable = Assertions.catchThrowable(() -> UsernameValidator.validate(username));
        // then
        Assertions.assertThat(catchThrowable).isInstanceOf(UserNameInvalidException.class);
    }

    static Stream<String> usernameOverLengthProvider() {
        return Stream.of("a".repeat(33));
    }
}