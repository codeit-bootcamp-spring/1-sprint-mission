package com.sprint.mission.discodeit.entity.user;

import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class UserNameTest {


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "T", "TT", "12345678901", "\n", "\t"})
    @DisplayName("요구되는 유저의 이름의 길이를 충족하지 못하는 value 제공 시 에러 발생 테스트")
    void givenUserNameLengthLessThanRequiredLengthWhenCreateUserThenThrowException(String name) {
        // given -> Parameter
        // when
        Throwable thrown = Assertions.catchThrowable(() -> new UserName(name));
        // then
        then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username");
    }
}