package com.sprint.mission.discodeit.entity.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.common.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("User Test")
class UserTest {

    private User user;

    @Test
    void createUserTest() {
        // given
        String userName = "test1";

        assertThat(new User(new UserName(userName))).isNotNull();
    }

    @Nested
    @DisplayName("유저 생성 수정 삭제")
    class InitializeNew {

        @BeforeEach
        void setUp() {
            user = new User(new UserName("test1"));
        }

        @Test
        @DisplayName("유저 객체를 생성한 후 객체의 name value 와 초기화 시 입력 값과 동일 테스트")
        void givenUserWhenGetUserNameThenReturnUserName() {
            // given

            // when
            var userName = user.getName();
            // then
            assertThat(user.getName()).isEqualTo("test1");
        }

        // TODO : 유저 이름 수정 성공 테스트

        // TODO : 유저 이름 수정 실패 테스트

        @Test
        @DisplayName("유저 해지 요청 후 유저의 상태 변경")
        void givenUnregisterRequestWhenUnregisterThenStatusChange() {
            // given
            // when
            user.unregister();
            // then
            assertThat(user.getStatus()).isEqualTo(Status.UNREGISTERED);
        }
    }


}
