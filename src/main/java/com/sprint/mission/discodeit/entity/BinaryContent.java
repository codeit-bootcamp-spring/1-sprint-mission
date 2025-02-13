package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient UUID id;
    private final Instant createdAt;
    private UUID userId;
    private UUID messageId;

    public BinaryContent() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = null;
        this.messageId = null;
    }

    public void saveUserProfileImage(UUID userId){
        this.userId = userId;
    }

    public void attachFileToMessage(UUID messageId){
        this.messageId = messageId;
    }
}
