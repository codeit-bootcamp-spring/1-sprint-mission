package com.sprint.mission.discodeit.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NicknameTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "User1",       // 일반적인 영어 이름
            "유저123",      // 한글 + 숫자 조합
            "emoji😊",     // 영어 + 이모지
            "닉네임_테스트", // 한글 + 밑줄
            "user_name",   // 영어 + 밑줄
            "유저_이름",    // 한글 + 밑줄
            "테스트123",    // 한글 + 숫자 조합
            "유저_😊",      // 한글 + 이모지
            "123456789012345", // 최대 길이 15글자
            "😊😊😊😊😊",     // 이모지만 사용 (최대 5글자 이모지)
            "name_123",    // 밑줄 + 숫자 조합
            "_under_score",// 밑줄로 시작 가능 여부
            "이름😊",       // 한글 + 이모지
            "USERNAME",    // 영어 대문자
            "User_😊_Name" // 영어 + 밑줄 + 이모지
    })
    void 유저_닉네임_생성_성공(String nickname) {
        //given
        // when
        Nickname createNickname = new Nickname(nickname);
        // then
        assertThat(createNickname.getValue()).isEqualTo(nickname);
    }

}