package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends AbstractEntity {
    private UUID userId;
    private UUID channelId;
    private String content;

    public Message(UUID userId, UUID channelId, String content) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
    }
    public UUID getUserId() {
        return userId;
    }
    public UUID getChannelId() {
        return channelId;
    }
    public String getContent() {
        return content;
    }

    public void update(String content) {
        this.content = content;
        updateTimestamp();
    }

    public void update(UUID userId, UUID channelId, String content) {
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
        updateTimestamp();
    }

}
