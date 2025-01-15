package com.sprint.mission.discodeit.entity.user;

import static com.sprint.mission.discodeit.entity.common.Status.UNREGISTERED;
import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.user.entity.User;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("User Test")
class UserTest {
    private static final String USER_NAME = "test";
    private static final String CHANNEL_NAME = "코드잇-스프린트_1기";
    private User user;

    @Test
    void createUserTest() {
        // given
        String userName = "test1";

        assertThat(User.createFrom(userName)).isNotNull();
    }

    @Nested
    @DisplayName("유저 생성 수정 삭제")
    class InitializeNew {
        @BeforeEach
        void setUp() {
            user = User.createFrom(USER_NAME);
        }

        @Test
        @DisplayName("유저 객체를 생성한 후 객체의 name value 와 초기화 시 입력 값과 동일 테스트")
        void givenUserWhenGetUserNameThenReturnUserName() {
            // given

            // when
            var userName = user.getName();
            // then
            assertThat(userName).isEqualTo(USER_NAME);
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
            assertThat(user.getStatus()).isEqualTo(UNREGISTERED);
        }
    }

    @Nested
    @DisplayName("유저 채널")
    class aboutChannel {
        private Channel participatedChannel;

        @BeforeEach
        void setup() {
            user = User.createFrom(USER_NAME);
            participatedChannel = user.openNewChannel("new Channel1");
        }

        @Test
        @DisplayName("채널 이름을 유저가 제공하여 새로운 채널을 만들면 생성된 채널 반환")
        void givenChannelNameWhenUserCreateChannelThenReturnChannel() {
            // given
            var createdUser = User.createFrom(USER_NAME);
            // when
            var channel = createdUser.openNewChannel(CHANNEL_NAME);
            // then
            Assertions.assertAll(
                    () -> {
                        assertThat(channel).isNotNull();
                        assertThat(channel.getChannelName()).isEqualTo(CHANNEL_NAME);
                    }
            );
        }

        @Test
        @DisplayName("새로운 채널 이름으로 유저가 채널의 이름을 변경 시 변경된 채널 반환")
        void givenChangeChannelNameWhenUserChangeChannelNameThenReturnChangedChannel() {
            // given
            var newChangeChannelName = "NEW CHANNEL NAME";
            var targetChannelId = participatedChannel.getId();
            // when
            var changeChannel = user.changeChannelName(targetChannelId, newChangeChannelName);
            // then
            assertThat(changeChannel.getChannelName()).isEqualTo(newChangeChannelName);
        }

        @Test
        @DisplayName("유저의 참가 채널 id로 참가한 채널에서 나가면 참가채널 수 감소")
        void givenExitChannelIdWhenExitParticipatedChannelThenReturnParticipatedChannelCountDecrease() {
            // given
            var participatedChannelId = participatedChannel.getId();
            var oldCount = user.countParticipatedChannels();
            // when
            user.exitParticipatedChannel(participatedChannelId);
            // then
            assertThat(user.countParticipatedChannels()).isEqualTo(oldCount - 1);
        }
    }

}
