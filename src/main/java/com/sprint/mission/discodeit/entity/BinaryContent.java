package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private final BelongType type;
    private final UUID belongTo;
    @Setter
    private String name;
    @Setter
    private String path;

    private BinaryContent(UUID id, BelongType type, UUID belongTo, String name, String path) {
        this.id = id;
        this.createdAt = Instant.now();
        this.type = type;
        this.belongTo = belongTo;
        this.name = name;
        this.path = path;
    }

    public static BinaryContent of(UUID id, BelongType type, UUID belongTo, String name, String path) {
        return new BinaryContent(id, type, belongTo, name, path);
    }
}
