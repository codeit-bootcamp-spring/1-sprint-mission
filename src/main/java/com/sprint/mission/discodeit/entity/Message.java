package com.sprint.mission.discodeit.entity;


import java.util.UUID;

public class Message extends Base{
    private String content;
    private String senderId;
    private String channelId;

    public Message(String content, String senderId, String channelId) {
        super();
        this.content = content;
        this.senderId = senderId;
        this.channelId = channelId;
    }

    public String getContent() {
        return content;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void updateContent(String content) {
        this.content = content;
        getUpdatedAt();
    }
}
