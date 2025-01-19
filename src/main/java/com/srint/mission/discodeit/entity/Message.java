package com.srint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{

    private String content;
    private final UUID userId;
    private final UUID channelId;


    public Message(UUID userId, UUID channelId, String content) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getChannelId() {
        return channelId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "msg='" + content + '\'' +
                ", userId=" + userId +
                ", channelId=" + channelId +
                '}'+"  "+ super.toString();
    }
}
