package com.sprint.mission.discodeit.domain.user.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.sprint.mission.discodeit.domain.user.exception.PassWordInvalidException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("passwordOverLengthProvider")
    @ValueSource(strings = {
            "Ab1!",          // 8자 미만
            "password1!",    // 대문자 없음
            "PASSWORD1!",    // 소문자 없음
            "Password!",     // 숫자 없음
            "Password1",     // 특수문자 없음
            "Password 1!",   // 공백 포함
            "Password1한",   // 한글 포함
            "12345678!",     // 숫자 + 특수문자만 (영문 없음)
            "ABCDEFGH1!",    // 대문자 + 숫자 + 특수문자만 (소문자 없음)
            "abcdefgh1!",    // 소문자 + 숫자 + 특수문자만 (대문자 없음)
            "Password blanck",
    })
    void 비밀번호_정규식_생성_검증_에러throw(String password) {
        //given
        // when
        Throwable catchThrow = catchThrowable(() -> PasswordValidator.validateOrThrow(password));
        // then
        assertThat(catchThrow).isInstanceOf(PassWordInvalidException.class);
    }

    static Stream<String> passwordOverLengthProvider() {
        return Stream.of("A1!a".repeat(26));
    }

}
