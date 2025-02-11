package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 17L;
    private final Instant createdAt;
    private final UUID id;
    private final UUID typeId;
    private final BinaryContentType type; //PROFILE, MESSAGE
    private final byte[] data;


    public BinaryContent(UUID typeId, BinaryContentType type, byte[] data) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.typeId = typeId;
        this.type = type;

        this.data = data != null ? data.clone() : null;
    }

}
