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
    private String fileName;
    private String contentType;
    private byte[] bytes;
    private ParentType parentType;
    private UUID userId;
    private UUID messageId;

    public enum ParentType {
        USER,
        MESSAGE
    }

    public static BinaryContent createBinaryContent(String fileName, String contentType, byte[] bytes, ParentType parentType, UUID parentId) {
        if (parentType == ParentType.USER) {
            return new BinaryContent(fileName, contentType, bytes, parentType, parentId, null);
        } else {
            return new BinaryContent(fileName, contentType, bytes, parentType, null, parentId);
        }
    }

    private BinaryContent(String fileName, String contentType, byte[] bytes, ParentType parentType, UUID userId, UUID messageId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.bytes = bytes;
        this.contentType = contentType;
        this.parentType = parentType;
        this.userId = userId;
        this.messageId = messageId;
    }


}
