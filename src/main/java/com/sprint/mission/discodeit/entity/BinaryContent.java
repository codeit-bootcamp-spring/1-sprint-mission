package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();

    private final String fileName;
    private final Long size;
    private final String contentType;
    private final byte[] bytes;

    private UUID userId;  // User 참조
    private UUID messageId;  // Message 참조

}
