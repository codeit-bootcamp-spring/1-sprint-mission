package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

public class MessageDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateMessageDTO {
        private String content;
        private UUID channelId;
        private UUID authorId;
        private List<UUID> attachedFileIds;  // 첨부파일 IDs
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UpdateMessageDTO {
        private String newContent;
    }
}
