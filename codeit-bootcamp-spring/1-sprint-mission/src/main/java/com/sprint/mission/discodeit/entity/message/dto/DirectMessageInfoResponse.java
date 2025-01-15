package com.sprint.mission.discodeit.entity.message.dto;

import java.util.UUID;

public record DirectMessageInfoResponse(UUID messageId, UUID sender, UUID receiver, String message) {

    public static final class Builder {
        private UUID messageId;
        private UUID sender;
        private UUID receiver;
        private String message;

        public Builder messageId(UUID messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder sender(UUID sender) {
            this.sender = sender;
            return this;
        }

        public Builder receiver(UUID receiver) {
            this.receiver = receiver;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public DirectMessageInfoResponse build() {
            return new DirectMessageInfoResponse(messageId, sender, receiver, message);
        }
    }
}
