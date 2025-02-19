package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.application.dto.user.joinUserRequestDto;
import com.sprint.mission.discodeit.application.service.user.JCFUserService;
import com.sprint.mission.discodeit.domain.user.exception.AlreadyUserExistsException;
import com.sprint.mission.discodeit.fake.FakeFactory;
import com.sprint.mission.discodeit.fake.domain.user.StubUser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private JCFUserService userService;
    private joinUserRequestDto joinUserReqeustDto = StubUser.generateJoinRequestDto();

    @BeforeEach
    void setup() {
        userService = FakeFactory.getUserService();
    }

    @Test
    void 회원가입_요청_메소드_호출_이미_존재하는_이메일_이면_에러throw() {
        // given
        userService.join(joinUserReqeustDto);
        joinUserRequestDto anotherUsernameRequest = StubUser.generateJoinRequestByUsername("anotherusername");

        // when
        Throwable catchThrow = Assertions.catchThrowable(
                () -> userService.join(anotherUsernameRequest));
        // then
        Assertions.assertThat(catchThrow).isInstanceOf(AlreadyUserExistsException.class);
    }

    @Test
    void 회원가입_요청_이미_존재하는_유저이름_요청_에러throw() {
        // given
        userService.join(joinUserReqeustDto);
        joinUserRequestDto anotherEmailRequest = StubUser.generateJoinRequestByEmail("another@test.com");

        // when
        Throwable catchThrow = Assertions.catchThrowable(
                () -> userService.join(anotherEmailRequest));
        // then
        Assertions.assertThat(catchThrow).isInstanceOf(AlreadyUserExistsException.class);
    }
}
