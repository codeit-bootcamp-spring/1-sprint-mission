package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class BinaryContent implements Serializable {
    private static final long getSerialVersionUID = 1L;
    private UUID id;
    private final Instant createdAt;
    private final String userId;
    private final String messageId;
    private final byte[] content;
    private final String mimetype;

    public BinaryContent(String userId, String messageId, byte[] content, String mimetype) {
        this.id = id != null ? id : UUID.randomUUID();
        this.createdAt = Instant.ofEpochMilli(System.currentTimeMillis());
        this.userId = userId;
        this.messageId = messageId;
        this.content = content;
        this.mimetype = mimetype;
    }
}
