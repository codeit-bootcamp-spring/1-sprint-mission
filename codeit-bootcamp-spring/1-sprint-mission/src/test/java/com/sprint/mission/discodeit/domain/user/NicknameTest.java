package com.sprint.mission.discodeit.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.sprint.mission.discodeit.domain.user.exception.NickNameInvalidException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
            "papago good",
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