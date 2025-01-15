package com.sprint.mission.discodeit.entity.user.entity;

import static com.sprint.mission.discodeit.common.error.ErrorMessage.USER_NOT_PARTICIPATED_CHANNEL;
import static com.sprint.mission.discodeit.entity.common.Status.MODIFIED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sprint.mission.discodeit.common.error.channel.ChannelException;
import com.sprint.mission.discodeit.entity.channel.Channel;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParticipatedChannelTest {
    private static final String CHANNEL_NAME = "코드잇-1기-백엔드";
    private ParticipatedChannel channels;
    private static final User USER = User.createFrom("SB_1기_백재우");

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
        var channel = channels.createChannel(CHANNEL_NAME, USER);
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
            createdChannel1 = channels.createChannel(addChannelName1, USER);
            createdChannel2 = channels.createChannel(addChannelName2, USER);
        }

        @Test
        @DisplayName("유저가 참여한 채널에서 Id를 통해 채널 조회")
        void givenSomeElementsAddedChannelsWhenFindByIdThenReturnSomeElements() {
            // given
            var channelId = createdChannel1.getId();

            // when
            var findByIdParticipatedChannel = channels.findByChannelIdNotUnregisteredOrThrow(channelId);

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
        @DisplayName("유저가 참여한 채널에 존재하지 않는 channel Id로 채널 조회 시 에러 발생")
        void givenNotExistChannelIdWhenFindByIdThenThrowException() {
            // given
            var notExistedChannelId = UUID.randomUUID();

            // when
            var throwable = catchThrowable(() ->
                    channels.findByChannelIdNotUnregisteredOrThrow(notExistedChannelId)
            );

            // then
            assertThat(throwable).isInstanceOf(ChannelException.class)
                    .hasMessageContaining(USER_NOT_PARTICIPATED_CHANNEL.getMessage());
        }

        @Test
        @DisplayName("유저가 참여한 채널에 존재하는 ChannelName 으로 채널 조회 시 해당 채널 반환")
        void givenExistedChannelNameWhenFindByNameThenReturnChannel() {
            // given

            // when
            var foundChannel = channels.findByName(addChannelName1);
            // then
            assertThat(foundChannel)
                    .isNotNull();
        }

        @Test
        @DisplayName("유저가 참여한 채널에 존재하지 않는 ChannelName 으로 채널 조회시 에러")
        void givenNotExistedChannelNameWhenFindByNameThenThrowException() {
            // given
            var notExistedChannelName = "SB_999기_백재우";

            // when
            var foundChannel = channels.findByName(notExistedChannelName);

            // then
            assertThat(foundChannel.orElse(null)).isNull();
        }

        @Test
        @DisplayName("유저가 참여한 채널의 id로 채널이름을 변경 성공")
        void givenParticipatedChannelIdAndNewChannelNameWhenChangeChannelNameThenSuccess() {
            // given
            var channelId = createdChannel1.getId();
            var changeChannelName = createdChannel1.getChannelName();

            // when
            channels.changeChannelNameOrThrow(channelId, "new ChannelName", USER);

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
            var throwable = catchThrowable(() -> channels.changeChannelNameOrThrow(notExistedChannelId, changeChannelName, USER));

            // then
            assertThat(throwable).isInstanceOf(ChannelException.class)
                    .hasMessageContaining(notExistedChannelId.toString());

        }

        @Test
        @DisplayName("채널을 생성한 사람이 아닌 다른 누군가 채널이름을 변경하려고 할 경우 에러")
        void givenNotChannelCreatorWhenChangeChannelNameThenThrowChannelException() {
            // given
            var notCreatorUser = User.createFrom("is not Creator");
            var changeChannelName = "SB_백엔드_1000기";
            var channelId = createdChannel1.getId();

            // when
            var throwable = catchThrowable(() ->
                    channels.changeChannelNameOrThrow(channelId, changeChannelName, notCreatorUser)
            );

            // then
            assertThat(throwable).isInstanceOf(ChannelException.class)
                    .hasMessageContaining(notCreatorUser.getName());
        }

        @Test
        @DisplayName("채널 id 값으로 유저가 참여한 채널을 삭제하면 유저가 참여한 채널 목록의 개수 감소")
        void givenDeleteTargetIdWhenDeleteChannelThenParticipatedChannelIsRemove() {
            // given
            var oldParticipatedChannelCount = channels.countParticipatedChannels();
            var deleteTargetChannelId = createdChannel2.getId();

            // when
            channels.exitChannelById(deleteTargetChannelId);

            // then
            assertThat(channels.countParticipatedChannels()).isNotEqualTo(oldParticipatedChannelCount);
        }
    }
}



