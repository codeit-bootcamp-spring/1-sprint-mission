package com.sprint.mission.discodeit.entity.channel.dto;

import java.util.UUID;

public record ChannelResponse(UUID channelId, String channelName, String creator, String status) {

//    public static ChannelResponse ofChannelIdNameCreatorStatus(UUID channelId, String channelName, String creator, String status) {
//        // 이게 맞나... 모든 파라미터를 null check
//        Preconditions.checkNotNull(channelId);
//        Preconditions.checkNotNull(channelName);
//        Preconditions.checkNotNull(creator);
//        Preconditions.checkNotNull(status);
//        return new ChannelResponse(channelId, channelName, creator, status);
//    }

    public static final class Builder {
        private UUID channelId;
        private String channelName;
        private String creator;
        private String status;

        public Builder() {}

        public Builder channelId(UUID channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder channelName(String channelName) {
            this.channelName = channelName;
            return this;
        }

        public Builder creator(String creator) {
            this.creator = creator;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public ChannelResponse build() {
            return new ChannelResponse(channelId, channelName, creator, status);
        }
    }
}
