package com.sprint.mission.discodeit.entity.user.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParticipatedChannelTest {
    private static final String CHANNEL_NAME = "코드잇-1기-백엔드";
    private ParticipatedChannel participatedChannel;

    @Test
    void givenNewParticipatedChannelWhenCreateThenReturnNewParticipatedChannel() {
        // given when
        var createdParticipatedChannel = ParticipatedChannel.newDefault();
        // then
        assertThat(createdParticipatedChannel).isNotNull();
    }
    // TODO  생성 테스트

    @Nested
    @DisplayName("채널 crud")
    class CrudParticipatedChannelTest {

        @BeforeEach
        void setUp() {
            participatedChannel = ParticipatedChannel.newDefault();
        }

        @Test
        void givenChannelNameWhenCreateChannelThenReturnNewChannel() {
            // given when
            var channel = participatedChannel.generateFrom(CHANNEL_NAME);
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
        // TODO 채널 찾기 테스트

        // TODO 채널 수정 테스트
    }

}