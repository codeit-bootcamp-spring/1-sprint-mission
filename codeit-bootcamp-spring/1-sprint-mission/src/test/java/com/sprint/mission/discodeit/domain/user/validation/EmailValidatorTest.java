package com.sprint.mission.discodeit.domain.user.validation;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.domain.user.exception.EmailInvalidException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailValidatorTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "abc",              // 일반 문자열
            "abc@",             // '@'만 포함
            "@example.com",     // 로컬 부분 없음
            "abc@example",      // 최상위 도메인 없음
            "abc@.com",         // 도메인 부분이 비정상적
            "abc@com.",         // 도메인 형식 오류
            "abc@@example.com", // '@' 중복
            "abc example.com",  // 공백 포함
            "abc@ex@ample.com", // 여러 개의 '@'
            "abc@example..com", // 연속된 '.' 포함
            "abc@example.c",    // 너무 짧은 TLD
            "abc@example#com",  // 특수 문자 포함
            "abc@.com",         // 도메인 앞부분 없음
            "abc@exam_ple.com", // 언더스코어 포함 (일반적으로 허용되지 않음)
    })
    void 이메일_생성_Invalid값_에러throw(String email) {
        //given
        // when
        Throwable throwable = Assertions.catchThrowable(() -> EmailValidator.valid(email));
        // then
        assertThat(throwable).isInstanceOf(EmailInvalidException.class);
    }

}