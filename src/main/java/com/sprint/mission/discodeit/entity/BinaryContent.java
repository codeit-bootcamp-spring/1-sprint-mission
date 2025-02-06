package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BinaryContent{
    private final UUID id;
    private final UUID userId;
    private final byte[] data;

    public BinaryContent(UUID userId, byte[] data) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.data = data;
    }
}
