package com.sprint.mission.discodeit.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;


@Getter @Setter
public class Message extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String content;
    private UUID senderId;
    private UUID recipientId;
    private UUID channelId;
    private BinaryContent attachedFileId;

    public Message(String content, UUID senderId, UUID recipientId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.channelId = channelId;
    }
}
