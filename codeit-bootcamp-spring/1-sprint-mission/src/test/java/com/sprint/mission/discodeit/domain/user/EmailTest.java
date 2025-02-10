package com.sprint.mission.discodeit.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.domain.user.exception.EmailInvalidException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "test@example.com",          // ✅ 일반적인 이메일
            "user.name@domain.co.kr",    // ✅ 도메인에 . 포함 가능
            "valid_email123@gmail.com",  // ✅ 숫자, 밑줄 포함 가능
            "email.with+symbol@domain.com", // ✅ `+` 포함 가능
            "firstname.lastname@example.com", // ✅ 마침표(`.`) 포함 가능
            "email@sub.domain.com",      // ✅ 서브 도메인 포함 가능
            "123456@example.org",        // ✅ 숫자만 포함 가능
            "user_name@company.net",     // ✅ 밑줄 포함 가능
            "email@domain.io",           // ✅ `io` 같은 도메인 가능
            "valid-email@domain.info",   // ✅ `-` 포함 가능
            "email@domain.tech",         // ✅ 최신 TLD (예: `.tech`) 지원
            "abc.def@ghi.jkl",           // ✅ 짧은 도메인 허용
            "valid-email@sub.domain.co.uk" // ✅ 여러 개의 서브도메인 가능
    })
    void 이메일_생성_성공(String email) {
        //given
        // when
        Email createdEmail = new Email(email);
        // then
        Assertions.assertThat(createdEmail.getValue()).isEqualTo(email);
    }

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
        Throwable throwable = Assertions.catchThrowable(() -> new Email(email));
        // then
        assertThat(throwable).isInstanceOf(EmailInvalidException.class);
    }
}