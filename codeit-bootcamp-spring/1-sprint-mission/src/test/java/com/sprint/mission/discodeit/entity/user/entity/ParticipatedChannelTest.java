package com.sprint.mission.discodeit.entity.user.entity;

import static com.sprint.mission.discodeit.common.error.ErrorMessage.USER_NOT_PARTICIPATED_CHANNEL;
import static com.sprint.mission.discodeit.entity.common.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sprint.mission.discodeit.common.error.user.UserException;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.common.Status;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParticipatedChannelTest {
    private static final String CHANNEL_NAME = "코드잇-1기-백엔드";
    private ParticipatedChannel channels;

    @Test
    void givenNewParticipatedChannelWhenCreateThenReturnNewParticipatedChannel() {
        // given when
        var createdParticipatedChannel = ParticipatedChannel.newDefault();
        // then
        assertThat(createdParticipatedChannel).isNotNull();
    }

    @BeforeEach
    void setUp() {
        channels = ParticipatedChannel.newDefault();
    }

    @Test
    void givenChannelNameWhenCreateChannelThenReturnNewChannel() {
        // given when
        var channel = channels.createChannel(CHANNEL_NAME);
        // then
        assertAll(
                () -> {
                    assertThat(channel).isNotNull();

                    assertAll(
                            () -> assertThat(channel.getChannelName()).isEqualTo(CHANNEL_NAME)
                    );
                }
        );
    }

    @Nested
    @DisplayName("유저의 참여 채널")
    class givenSomeElements {
        private static final String addChannelName1 = "스프린트_2기_백엔드";
        private static final String addChannelName2 = "스프린트_3기_백엔드";
        private Channel createdChannel1;
        private Channel createdChannel2;

        @BeforeEach
        void add() {
            createdChannel1 = channels.createChannel(addChannelName1);
            createdChannel2 = channels.createChannel(addChannelName2);
        }

        @Test
        @DisplayName("유저가 참여한 채널에서 Id를 통해 채널 조회")
        void givenSomeElementsAddedChannelsWhenFindByIdThenReturnSomeElements() {
            // given
            var channelId = createdChannel1.getId();
            // when
            var findByIdParticipatedChannel = channels.findById(channelId);
            // then
            assertAll(
                    () -> {
                        assertThat(findByIdParticipatedChannel).isNotNull();

                        assertAll(
                                () -> assertThat(findByIdParticipatedChannel.orElseThrow().getId())
                                        .isEqualTo(channelId)
                        );
                    }
            );
        }

        @Test
        @DisplayName("유저가 참여한 채널에서 channelName 을 통해 채널 조회")
        void givenSomeElementsAddedChannelsWhenFindByNameThenReturnSomeElements() {
            // given
            var channelName = createdChannel1.getChannelName();
            // when
            var findByIdParticipatedChannel = channels.findByName(channelName);
            // then
            assertAll(
                    () -> {
                        assertThat(findByIdParticipatedChannel).isNotNull();

                        assertAll(
                                () -> assertThat(findByIdParticipatedChannel.orElseThrow().getChannelName())
                                        .isEqualTo(addChannelName1)
                        );
                    }
            );
        }

        @Test
        @DisplayName("유저가 참여한 채널에 존재하지 않는 Id로 채널 조회 시 에러 발생")
        void givenNotExistChannelIdWhenFindByIdThenThrowException() {
            // given
            var notExistedId = UUID.randomUUID();

            // when
            var throwable = catchThrowable(() ->
                    channels.findById(notExistedId).orElseThrow(() ->
                            UserException.of(USER_NOT_PARTICIPATED_CHANNEL))
            );

            // then
            assertThat(throwable).isInstanceOf(UserException.class)
                    .hasMessageContaining(USER_NOT_PARTICIPATED_CHANNEL.getMessage());
        }

        @Test
        @DisplayName("유저가 참여한 채널에 존재하지 않는 ChannelName 으로 채널 조회시 에러")
        void givenNotExistedChannelNameWhenFindByNameThenThrowException() {
            // given
            var notExistedChannelName = "SB_999기_백재우";

            // when
            var throwable = catchThrowable(() ->
                    channels.findByName(notExistedChannelName).orElseThrow(() ->
                                    UserException.errorMessageAndChannelName(USER_NOT_PARTICIPATED_CHANNEL, notExistedChannelName)
                    )
            );
            // then
            assertThat(throwable).isInstanceOf(UserException.class)
                    .hasMessageContaining(USER_NOT_PARTICIPATED_CHANNEL.getMessage());
        }

        // TODO 채널 수정 테스트
        @Test
        @DisplayName("유저가 참여한 채널의 id로 채널이름을 변경 성공")
        void givenParticipatedChannelIdAndNewChannelNameWhenChangeChannelNameThenSuccess() {
            // given
            var channelId = createdChannel1.getId();
            var changeChannelName = createdChannel1.getChannelName();
            // when
            channels.changeChannelName(channelId, "new ChannelName");
            // then
            assertAll(
                    () -> {
                        assertThat(createdChannel1.getChannelName()).isNotEqualTo(changeChannelName);
                        assertThat(createdChannel1.getStatus()).isEqualTo(MODIFIED);
                    }
            );
        }

        @Test
        @DisplayName("존재하지 않는 채널 id로 유저가 참여한 채널의 id로 채널이름 변경 시 에러")
        void givenNotExistedChannelIdWhenChangeChannelNameThenThrowUserException() {
            // given
            var notExistedChannelId = UUID.randomUUID();
            var changeChannelName = "new ChannelName";

            // when
            var throwable = catchThrowable(() -> channels.changeChannelName(notExistedChannelId, changeChannelName));
            // then
            assertThat(throwable).isInstanceOf(UserException.class)
                    .hasMessageContaining(notExistedChannelId.toString());

        }
    }

}



