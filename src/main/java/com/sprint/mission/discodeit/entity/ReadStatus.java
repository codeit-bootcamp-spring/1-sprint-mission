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
public class ReadStatus {

    /**
     * Field: {@code EMPTY_READ_STATUS} is literally empty static ReadStatus object
     */
    public static final ReadStatus EMPTY_READ_STATUS;
    private final UUID userId;
    private final Instant createAt;
    private final Instant updateAt;

    static {
        EMPTY_READ_STATUS = new ReadStatus(
            UUID.fromString(EMPTY_UUID.getValue()),
            Instant.parse(EMPTY_TIME.getValue()),
            Instant.parse(EMPTY_TIME.getValue())
        );
    }

    public static ReadStatus createReadStatus() {
        return new ReadStatus(UUID.randomUUID(), Instant.now(), Instant.now());
    }

    public static ReadStatus createReadStatus(UUID id) {
        return new ReadStatus(id, Instant.now(), Instant.now());
    }

    public static ReadStatus createReadStatus(UUID id, Instant createAt, Instant updateAt) {
        return new ReadStatus(id, createAt, updateAt);
    }
}
