package com.sprint.mission.discodeit.entity.message;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageSendFileRequest{
    private final UUID userId;
    private final UUID messageId;
    private final String fileName;
    private final String fileType;
    public MessageSendFileRequest(UUID userId, String fileName, String fileType) {
        this.userId = userId;
        this.messageId = UUID.randomUUID();
        this.fileName = fileName;
        this.fileType = fileType;
    }
}
