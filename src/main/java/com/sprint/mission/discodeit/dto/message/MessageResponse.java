package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    private UUID id;
    private UUID channelId;
    private UUID authorId;
    private String senderUsername;
    private String content;
    private Instant createdAt;
    private List<UUID> attachments;

    public MessageResponse(Message message, String senderUsername, List<UUID> attachments) {
        this.id = message.getId();
        this.channelId = message.getChannelId();
        this.authorId = message.getAuthorId();
        this.senderUsername = senderUsername;
        this.content = message.getContent();
        this.createdAt = message.getCreatedAt();
        this.attachments = attachments;
    }
}
