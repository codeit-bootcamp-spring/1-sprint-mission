package com.sprint.mission.discodeit.entity;


import java.util.UUID;

public class Message extends Base{
    private String content;
    private UUID senderId;
    private UUID channelId;

    public Message(String content, UUID senderId, UUID channelId) {
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
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
