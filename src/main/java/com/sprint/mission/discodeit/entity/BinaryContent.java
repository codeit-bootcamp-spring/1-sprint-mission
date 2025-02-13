package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;

@Getter
public class BinaryContent {
    private String id;
    private Instant createdAt;
    private Byte[] binaryImage;
}
