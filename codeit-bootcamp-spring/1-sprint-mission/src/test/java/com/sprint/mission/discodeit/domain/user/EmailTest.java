package com.sprint.mission.discodeit.domain.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
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
}