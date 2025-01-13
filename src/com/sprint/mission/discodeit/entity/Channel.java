package com.sprint.mission.discodeit.entity;

import java.util.UUID;

import static com.sprint.mission.discodeit.constant.IntegerConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

public class Channel extends BaseEntity {
    private static final Channel EMPTY_CHANNEL;
    static {
        EMPTY_CHANNEL = createChannel(
                UUID.fromString(EMPTY_UUID.getValue()),
                (long) EMPTY_TIME.getValue(),
                (long) EMPTY_TIME.getValue(),
                EMPTY_STRING.getValue()
        );
    }

    private final String name;

    private Channel(String name) {
        this(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                name
        );
    }
    private Channel(UUID id, String name) {
        this(
                id,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                name
        );
    }
    private Channel(UUID id, Long createAt, String name) {
        this(
                id,
                createAt,
                System.currentTimeMillis(),
                name
        );
    }
    private Channel(UUID id, Long createAt, Long updateAt, String name) {
        super(
                id,
                createAt,
                updateAt
        );
        this.name = name;
    }

    public static Channel createChannel(String name) {
        return new Channel(name);
    }
    public static Channel createChannel(UUID id, String name) {
        return new Channel(id, name);
    }
    public static Channel createChannel(UUID id, Long createAt, String name) {
        return new Channel(id, createAt, name);
    }
    public static Channel createChannel(UUID id, Long createAt, Long updateAt, String name) {
        return new Channel(id, createAt, updateAt, name);
    }
    public static Channel createEmptyChannel() {
        return EMPTY_CHANNEL;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                "} " + System.lineSeparator() +
                super.toString();
    }
}
