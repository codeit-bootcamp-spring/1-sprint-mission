package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity{

    private final UUID messageUuid;
    private final Long createdAt;
    private Long updatedAt;
    private final String userId;
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


    public void setMessageText(String messageText) {
        if(messageText == null || messageText.isEmpty()){
            throw new IllegalArgumentException("messageText cannot be null or empty");
        }
        this.messageText = messageText;
        updateUpdatedAt();
    }

}
