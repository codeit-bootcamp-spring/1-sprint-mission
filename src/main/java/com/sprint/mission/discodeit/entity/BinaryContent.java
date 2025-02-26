package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;

    private final Type type;
    private final UUID belongTo;
    private final String name;
    private final String path;

    public enum Type {
        PROFILE, MESSAGE
    }

    private BinaryContent(UUID id, Type type, UUID belongTo, String name, String path) {
        this.id = id;
        this.createdAt = Instant.now();
        this.type = type;
        this.belongTo = belongTo;
        this.name = name;
        this.path = path;
    }

    public static BinaryContent of(UUID id, Type type, UUID belongTo, String name, String path) {
        return new BinaryContent(id, type, belongTo, name, path);
    }
}
