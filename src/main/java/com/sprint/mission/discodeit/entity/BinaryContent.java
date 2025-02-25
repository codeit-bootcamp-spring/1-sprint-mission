package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {
    private String id;
    private Instant createdAt;
    private byte[] binaryImage;

    public BinaryContent(byte[] binaryImage, Instant createdAt) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = createdAt;
        this.binaryImage = binaryImage;
    }

}
