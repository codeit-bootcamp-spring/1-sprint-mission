package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.domain.Mimetype;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long getSerialVersionUID = 1L;
    private UUID id;
    private final Instant createdAt;
    private final UUID typeId;
//    private final byte[] content;
    private final Mimetype mimetype;

    public BinaryContent(UUID typeId, Mimetype mimetype) {
        this.id = id != null ? id : UUID.randomUUID();
        this.createdAt = Instant.ofEpochMilli(System.currentTimeMillis());
        this.typeId = typeId;
//        this.content = content;
        this.mimetype = mimetype;
    }
}
