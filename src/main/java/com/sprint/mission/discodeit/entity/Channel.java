package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
@Accessors(fluent = true)
public class Channel implements Serializable {

    @Getter(AccessLevel.NONE)
    @Serial
    private static final long serialVersionUID = 5112808545762685190L;

    /**
     * Field: {@code EMPTY_CHANNEL} is literally empty static Channel object
     */
    public static final Channel EMPTY_CHANNEL;
    private final UUID id;
    private final Instant createAt;
    private final Instant updateAt;
    private final String name;

    static {
        EMPTY_CHANNEL = createChannel(
            UUID.fromString(EMPTY_UUID.getValue()),
            Instant.parse(EMPTY_TIME.getValue()),
            Instant.parse(EMPTY_TIME.getValue()),
            EMPTY_STRING.getValue()
        );
    }

    public static Channel createChannel(String name) {
        return new Channel(UUID.randomUUID(), Instant.now(), Instant.now(), name);
    }

    public static Channel createChannel(UUID id, String name) {
        return new Channel(id, Instant.now(), Instant.now(), name);
    }

    public static Channel createChannel(UUID id, Instant createAt, String name) {
        return new Channel(id, createAt, Instant.now(), name);
    }

    public static Channel createChannel(UUID id, Instant createAt, Instant updateAt, String name) {
        return new Channel(id, createAt, updateAt, name);
    }
}
