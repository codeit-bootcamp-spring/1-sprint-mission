package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record MessageDTO() {
    public static class createMEssageRequestDTO {
        private String content;
        private UUID channelId;
        private UUID authorId;
    }

    public static class messageResponseDTO {
        private UUID messageId;
        private String content;
        private UUID channelId;
        private UUID authorId;
        private Long createdAt;
        private Long updatedAt;
    }

    public static class updateMessageRequestDTO {
        private String content;
    }
    public static class deleteMessageRequestDTO {
        private UUID messageId;
    }
}