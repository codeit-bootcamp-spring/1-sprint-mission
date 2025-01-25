package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{

    private final UUID messageUuid;
    private final Long createdAt;
    private Long updatedAt;
    private String userId;
    private String messageText;

    public Message(String userId, String messageText) {
        this.messageUuid = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.userId = userId;
        this.messageText = messageText;
    }

    // Getters
    public UUID getMessageUuid() {
        return messageUuid;
    }



    public String getUserId() {
        return userId;
    }

    public String getMessageText() {
        return messageText;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
        updateUpdatedAt();
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
        updateUpdatedAt();
    }

}
