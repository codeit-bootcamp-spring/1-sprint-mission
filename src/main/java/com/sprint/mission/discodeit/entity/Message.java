package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter @Setter
@Builder
@AllArgsConstructor
public class Message extends BaseEntity{
    private String content;
    private UUID senderId;
    private UUID recipientId;
    private UUID channelId;
    private BinaryContent attachedFileId;

    protected Message() { }

    public Message(String content, UUID senderId, UUID recipientId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.channelId = channelId;
    }
}
