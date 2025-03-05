package com.sprint.mission.discodeit.entity.channel;

import com.sprint.mission.discodeit.entity.user.User;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ChannelCommonRequest {
        private final UUID channelId;
        private String channelName;
        private User owner;

        public ChannelCommonRequest(UUID channelId, String channelName, User owner) {
                this.channelId = channelId;
                this.channelName = channelName;
                this.owner = owner;
        }
}