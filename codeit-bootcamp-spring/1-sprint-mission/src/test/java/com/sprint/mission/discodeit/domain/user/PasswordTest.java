package com.sprint.mission.discodeit.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "A1b@5678",       // 최소 길이(8) 만족
            "Secure#99",      // 대문자+소문자+숫자+특수문자 포함
            "StrongP@ssw0rd", // 일반적인 강력한 비밀번호
            "Aa1!Aa1!Aa1!",   // 패턴 반복
            "P@55w0rD2024",   // 숫자 포함된 조합
            "Valid123!@#",    // 숫자 + 특수문자 여러 개 포함
            "Pass1234!",      // 일반적인 패턴
            "LONGpassword123$LongPassword", // 대문자, 소문자, 숫자, 특수문자 포함 (길이 32자)
            "Xx1!yY2@zZ3#",   // 랜덤한 조합
            "C0mpl!c@tedPwd99$", // 다양한 조합
            "ABCDEFGh1@",     // 대문자 여러 개, 소문자 하나 포함
            "abcdEFG123$",    // 대소문자, 숫자, 특수문자 조합
            "1Password!",     // 일반적인 패턴
            "P@ssw0rddSecure123!", // 긴 길이 (최대 100자는 넘지 않음)
    })
    void 비밀번호_생성_성공(String password) {
        //given
        // when
        Password createdPassword = new Password(password);
        // then
        assertThat(createdPassword.getValue()).isEqualTo(password);
    }

}