package com.sprint.mission.discodeit.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateMessageRequestDto {
    private UUID messageId;
    private String newContent;

    public UpdateMessageRequestDto(UUID messageId, String newContent) {
        this.messageId = messageId;
        this.newContent = newContent;
    }
}
