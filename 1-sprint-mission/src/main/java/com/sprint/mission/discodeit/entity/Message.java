package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

public class Message extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

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
    public String getMessageUuid() {
        return messageUuid.toString();
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
