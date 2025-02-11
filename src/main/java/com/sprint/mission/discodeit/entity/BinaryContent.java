package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class BinaryContent {
    private UUID id;
    private UUID userId;
    private UUID messageId;
    private byte[] content;
    private String fileName;
    private String fileType;
    private Instant createdAt;
}
