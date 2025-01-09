package com.sprint.mission.discodeit.entity.user;

import static com.sprint.mission.discodeit.common.error.user.UserErrorMessage.NAME_LENGTH_ERROR_MESSAGE;
import static com.sprint.mission.discodeit.common.error.user.UserErrorMessage.USER_NAME_NULL;
import static com.sprint.mission.discodeit.entity.user.UserName.NAME_MAX_LENGTH;
import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class UserNameTest {

    private UserName userName;

    static Stream<String> stringProvider() {
        return Stream.of(
                ".".repeat(NAME_MAX_LENGTH + 1),
                "T",
                "TT"
        );
    }


    @ParameterizedTest(name = "[test {index}] ==> given name : {arguments}")
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\n", "\t"})
    @DisplayName("요구되는 유저의 이름이 비어있는 value 제공 시 에러 발생 테스트")
    void givenUserNameLengthLessThanRequiredLengthWhenCreateUserThenThrowException(String name) {
        // given -> Parameter
        // when
        Throwable thrown = catchThrowable(() -> new UserName(name));
        // then
        then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(USER_NAME_NULL.getMessage());
    }

    @ParameterizedTest(name = "given name : {arguments}")
    @MethodSource("stringProvider")
    @DisplayName("유저의 이름의 제한을 넘어가는 문자열로 생성 시 에러 발생 테스트")
    void givenMoreThanInvalidLengthNameWhenCreateUserNameThenThrowException(String overLengthName) {
        // given

        // when
        var throwable = catchThrowable(() -> new UserName(overLengthName));

        // then
        then(throwable).as("user name length limit check %d", NAME_MAX_LENGTH)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(NAME_LENGTH_ERROR_MESSAGE.getMessage());
    }


    @Nested
    @DisplayName("초기화 후 기능 테스트")
    class whenSetup {

        private static final String USER_NAME = "SB_1기_백재우";

        @BeforeEach
        void setup() {
            userName = new UserName(USER_NAME);
        }

        @Test
        @DisplayName("특정 문자열을 통해 UserName 객체 생성 후 동일한 문자열을 리턴 테스트")
        void givenNameWhenCreateUserNameThenReturnUserName() {
            then(userName.getName()).isEqualTo(USER_NAME);
        }

        @Test
        @DisplayName("새로운 이름으로 사용자 이름을 변경 시 새로운 UserName 객체 반환")
        void givenNewUserNameWhenChangeUserNameThenReturnNewUserName() {
            // given
            String newName = "SB_2기_백재우";
            // when
            var newUserName = userName.changeName(newName);
            // then
            then(newUserName).isNotEqualTo(userName);
        }
    }
}