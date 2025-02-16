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
public class Message implements Serializable {

    @Getter(AccessLevel.NONE)
    @Serial
    private static final long serialVersionUID = 1549358720773946438L;

    /**
     * Field: {@code EMPTY_MESSAGE} is literally empty static Message object
     */
    public static final Message EMPTY_MESSAGE;
    private final UUID id;
    private final Instant createAt;
    private final Instant updateAt;
    private final String content;

    static {
        EMPTY_MESSAGE = createMessage(
            UUID.fromString(EMPTY_UUID.getValue()),
            Instant.parse(EMPTY_TIME.getValue()),
            Instant.parse(EMPTY_TIME.getValue()),
            EMPTY_STRING.getValue()
        );
    }

    public static Message createMessage(String content) {
        return new Message(UUID.randomUUID(), Instant.now(), Instant.now(), content);
    }

    public static Message createMessage(UUID id, String content) {
        return new Message(id, Instant.now(), Instant.now(), content);
    }

    public static Message createMessage(UUID id, Instant createAt, String content) {
        return new Message(id, createAt, Instant.now(), content);
    }

    public static Message createMessage(UUID id, Instant createAt, Instant updateAt,
        String content) {
        return new Message(id, createAt, updateAt, content);
    }
}
