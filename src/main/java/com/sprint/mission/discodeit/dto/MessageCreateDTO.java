package com.sprint.mission.discodeit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateDTO {
    private UUID senderId;
    private UUID channelId;
    private String content;
    private UUID attachmentId;

    public MessageCreateDTO(UUID senderId, UUID channelId, String content) {
        this.senderId = senderId;
        this.channelId = channelId;
        this.content = content;
        this.attachmentId = null;
    }
}
