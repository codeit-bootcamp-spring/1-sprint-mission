package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public class BinaryContent {

    /**
     * Field: {@code EMPTY_BINARY_CONTENT} is literally empty static BinaryContent object
     */
    public static final BinaryContent EMPTY_BINARY_CONTENT;
    private final UUID id;
    private final Instant createAt;
    private final Instant updateAt;

    static {
        EMPTY_BINARY_CONTENT = new BinaryContent(
            UUID.fromString(EMPTY_UUID.getValue()),
            Instant.parse(EMPTY_TIME.getValue()),
            Instant.parse(EMPTY_TIME.getValue())
        );
    }

    public static BinaryContent createBinaryContent() {
        return new BinaryContent(UUID.randomUUID(), Instant.now(), Instant.now());
    }

    public static BinaryContent createBinaryContent(UUID id) {
        return new BinaryContent(id, Instant.now(), Instant.now());
    }

    public static BinaryContent createBinaryContent(UUID id, Instant createAt, Instant updateAt) {
        return new BinaryContent(id, createAt, updateAt);
    }

}
