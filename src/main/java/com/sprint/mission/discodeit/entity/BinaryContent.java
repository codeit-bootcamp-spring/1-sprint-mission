package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BinaryContent implements Serializable {

    @Serial
    private static final long serialVersionUID = -2633871231984110000L;

    private final UUID id;
    private final Instant createdAt;
    //
    private final String fileName;
    private final Long size;
    private final String contentType;
    private final byte[] bytes;

    public static BinaryContent createBinaryContent(String fileName, Long size, String contentType,
        byte[] bytes) {
        return new BinaryContent(UUID.randomUUID(), Instant.now(), fileName, size, contentType,
            bytes);
    }
}
