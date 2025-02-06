package com.sprint.mission.discodeit.dto.message;

import lombok.Getter;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;
import java.util.UUID;

@Getter
public class CreateMessageRequest {
    private String content;
    private UUID channelId;
    private UUID authorId;
    private List<byte[]> attachments;

    public CreateMessageRequest(String content, UUID channelId, UUID authorId, List<byte[]> attachments) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
        this.attachments = attachments;
    }

    public CreateMessageRequest(String content, UUID channelId, UUID authorId) {
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }
}
