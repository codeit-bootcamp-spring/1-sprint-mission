package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private Instant createdAt;
    private String content;
    private ParentType parentType;
    private UUID userId;        // parentId로 합칠 것인지.
    private UUID messageId;     //

    public enum ParentType {
        USER,
        MESSAGE
    }

    public BinaryContent createBinaryContent(String content, ParentType parentType, UUID userId, UUID messageId) {
        return new BinaryContent(content, parentType, userId, messageId);
    }

    private BinaryContent(String content, ParentType parentType, UUID userId, UUID messageId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.content = content;
        this.parentType = parentType;
        this.userId = userId;
        this.messageId = messageId;
    }


}
