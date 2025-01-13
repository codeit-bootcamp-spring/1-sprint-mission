package com.sprint.mission.discodeit.entity.user;

import static com.sprint.mission.discodeit.entity.user.entity.UserName.NAME_MAX_LENGTH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import com.sprint.mission.discodeit.entity.user.entity.UserName;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class UserNameTest {

    private UserName userName;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    static Stream<String> stringProvider() {
        return Stream.of(
                ".".repeat(NAME_MAX_LENGTH + 1),
                "T",
                "TT"
        );
    }

    @ParameterizedTest(name = "[test {index}] ==> given name : {arguments}")
    @NullAndEmptySource
    @DisplayName("요구되는 유저의 이름이 비어있는 value 제공 시 에러 발생 테스트")
    void givenUserNameLengthLessThanRequiredLengthWhenCreateUserThenThrowException(String name) {
        // given
        userName = UserName.createFrom(name);
        Set<ConstraintViolation<UserName>> violations = validator.validate(userName);
        // then
        assertThat(violations.size()).isEqualTo(1);
    }

    @ParameterizedTest(name = "given name : {arguments}")
    @MethodSource("stringProvider")
    @DisplayName("유저의 이름의 제한을 넘어가는 문자열로 생성 시 에러 발생 테스트")
    void givenMoreThanInvalidLengthNameWhenCreateUserNameThenThrowException(String overLengthName) {
        // given
        userName = UserName.createFrom(overLengthName);
        Set<ConstraintViolation<UserName>> violations = validator.validate(userName);
        // then
        assertThat(violations.size()).isEqualTo(1);
    }


    @Nested
    @DisplayName("초기화 후 기능 테스트")
    class whenSetup {

        private static final String USER_NAME = "SB_1기_백재우";

        @BeforeEach
        void setup() {
            userName = UserName.createFrom(USER_NAME);
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