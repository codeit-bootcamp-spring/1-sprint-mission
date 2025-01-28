package com.sprint.mission.discodeit.entity.channel;

import static com.sprint.mission.discodeit.entity.common.Status.MODIFIED;
import static com.sprint.mission.discodeit.entity.common.Status.UNREGISTERED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sprint.mission.discodeit.entity.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("채널 도메인")
class ChannelTest {
    private static final String CHANNEL_NAME = "스프린트_백엔드_1기";
    private static final String USER_NAME = "SB_1기_백재우";
    private static final User USER = User.createFrom(USER_NAME);
    private Channel channel;

    @Test
    @DisplayName("새로운 채널 생성")
    void createChannelThenSuccess() {
        assertThat(Channel.createOfChannelNameAndUser(CHANNEL_NAME, USER))
                .isNotNull();
    }

    @Nested
    @DisplayName("메서드 검사")
    class whenUseMethodTest {

        @BeforeEach
        void setUp() {
            channel = Channel.createOfChannelNameAndUser(CHANNEL_NAME, USER);
        }

        @Test
        @DisplayName("변경하려는 새로운 이름으로 채널이름 변경하면 채널의 이름과 상태가 변경")
        void givenChangeChannelNameWhenChangeNameThenUpdateStatusAndChannelName() {
            // given
            var newChannelName = "SB_999기_백재우";
            // then
            channel.changeName(newChannelName, USER);
            // when
            assertAll(
                    () -> {
                        assertThat(channel.getChannelName()).isNotEqualTo(CHANNEL_NAME);
                        assertThat(channel.getStatus()).isEqualTo(MODIFIED);
                    }
            );
        }

        @Test
        @DisplayName("채널을 삭제 시 상태가 변경")
        void givenChannelWhenDeleteChannelThenStatusUpdate() {
            // given
            // when
            channel.deleteChannel(USER);
            // then
            assertThat(channel.getStatus()).isEqualTo(UNREGISTERED);
        }

        @Test
        @DisplayName("매개변수로 들어온 이름과 채널 객체의 이름이 같을 경우, 상태가 해지 상태가 아닐 시 True 반환")
        void givenSameChannelNameWhenIsEqualFromNameAndNotUnregisteredThenTrue() {
            // given

            // when
            var validResult = channel.isRegisteredAndNameEqual(CHANNEL_NAME);
            // then
            assertThat(validResult).isTrue();
        }

        @Test
        @DisplayName("매개변수로 들어온 이름과 채널 객체의 이름이 다를 경우, 상태가 해지 상태가 아닐 시 False 반환")
        void givenDifferentChannelNameWhenIsEqualFromNameAndNotUnregisteredThenFalse() {
            // given
            var differentChannelName = "스프린트_백엔드_100기";
            // when
            var validResult = channel.isRegisteredAndNameEqual(differentChannelName);
            // then
            assertThat(validResult).isFalse();
        }

        @Test
        @DisplayName("찾으려는 채널이름과 비교하려는 채널 객체의 이름이 같고 해지상태가 아닐 경우 true 반환")
        void givenSameNameWhenIsStatusNotUnregisteredAndEqualsToThenTrue() {
            // given
            var findChannelNameRequest = CHANNEL_NAME;
            // when
            var isEquals = channel.isRegisteredAndNameEqual(findChannelNameRequest);
            // then
            assertThat(isEquals).isTrue();
        }

        @Test
        @DisplayName("찾으려는 채널이름과 비교하려는 채널 객체 이름이 같고 해지상태일 경우 false 반환")
        void givenSameNameAndUnregisteredWhenIsStatusNotUnregisteredThenFalse() {
            // given
            var findChannelNameRequest = CHANNEL_NAME;
            channel.updateUnregistered();
            // when
            var isEquals = channel.isRegisteredAndNameEqual(findChannelNameRequest);
            // then
            assertThat(isEquals).isFalse();
        }
    }
}