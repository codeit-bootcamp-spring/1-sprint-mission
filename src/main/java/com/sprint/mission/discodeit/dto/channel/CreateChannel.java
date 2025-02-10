package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.util.type.ChannelFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

public class CreateChannel {
    @Getter
    @AllArgsConstructor
    public static class PrivateRequest {
        private List<User> joinUser;
        private ChannelFormat channelFormat;
    }

    @Getter
    @AllArgsConstructor
    public static class PublicRequest {
        private String name;
        private String description;
        ChannelFormat channelFormat;
        private List<User> joinUser;
    }
}
