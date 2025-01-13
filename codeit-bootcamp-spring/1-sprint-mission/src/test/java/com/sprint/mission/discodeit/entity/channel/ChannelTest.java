package com.sprint.mission.discodeit.entity.channel;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("채널 도메인")
class ChannelTest {
    private static final String CHANNEL_NAME = "스프린트_백엔드_1기";
    private static final String USER_NAME = "SB_1기_백재우";
    private User user = User.createFrom(USER_NAME);

    @Test
    @DisplayName("새로운 채널 생성")
    void createChannelThenSuccess() {
        assertThat(Channel.createFromChannelNameAndUser(CHANNEL_NAME, user))
                .isNotNull();
    }

    @Nested
    @DisplayName("생성, 수정, 삭제, 조회")
    class whenCRUD {

    }
}