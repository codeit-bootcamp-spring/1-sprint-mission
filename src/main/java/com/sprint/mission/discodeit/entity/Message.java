package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter @Setter
@Builder
//@AllArgsConstructor
public class Message extends BaseUpdatableEntity{

    @Column(name = "content")
    private String content;

    @Column(name = "sender_id")
    private UUID senderId;

    @Column(name = "recipient_id")
    private UUID recipientId;

    @Column(name = "channel_id")
    private UUID channelId;

//    @ManyToOne
//    @JoinColumn(name = "binary_content_id")
//    private BinaryContent attachedFileId;

    protected Message() { }

    public Message(String content, UUID senderId, UUID recipientId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.channelId = channelId;
    }
}
