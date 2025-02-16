package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
public class Message extends BaseEntity implements Serializable {
    private String content;
    private UUID senderId;
    private UUID channelId;

    private BinaryContent attachFile;
    private List<BinaryContent> messageFiles;

    public Message(String content, UUID senderId, UUID channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }
    //깊은 복사위한 복사 생성자
    public Message(Message message) {
        this.content = message.getContent();
        this.channelId = message.channelId;
        this.senderId=message.senderId;
    }
    public void updateMessage(String content) {
        this.content = content;
        setUpdatedAt();
    }
    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}
