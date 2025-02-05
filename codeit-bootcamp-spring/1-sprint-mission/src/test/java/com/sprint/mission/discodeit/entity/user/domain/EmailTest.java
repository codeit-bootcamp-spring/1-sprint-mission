package com.sprint.mission.discodeit.entity.user.domain;

import static org.assertj.core.api.Assertions.*;

import com.sprint.mission.discodeit.domain.user.Email;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"test.com"})
    void 잘못된_이메일_형식_생성시_에러throw(String email) {
        //given
        // when
        Throwable catchThrow = catchThrowable(() -> new Email(email));
        // then
        assertThat(catchThrow).isInstanceOf(IllegalArgumentException.class);
    }

}