package com.sprint.mission.discodeit.entity.message.dto;

import java.util.UUID;

public record ChannelMessageInfoResponse(UUID messageId, UUID sendUserId, UUID receiveChannelId, String message) {

    public static final class Builder {
        private UUID messageId;
        private UUID sendUserId;
        private UUID receiveChannelId;
        private String message;

        public Builder messageId(UUID messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder sendUserId(UUID sendUserId) {
            this.sendUserId = sendUserId;
            return this;
        }

        public Builder receiveChannelId(UUID receiveChannelId) {
            this.receiveChannelId = receiveChannelId;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public ChannelMessageInfoResponse build() {
            return new ChannelMessageInfoResponse(messageId, sendUserId, receiveChannelId, message);
        }
    }
}
