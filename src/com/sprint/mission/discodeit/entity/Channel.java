package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.IntegerConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 5112808545762685190L;

    /** Field: {@code EMPTY_CHANNEL} is literally empty static Channel object */
    public static final Channel EMPTY_CHANNEL;
    private final UUID   id;
    private final Long   createAt;
    private final Long   updateAt;
    private final String name;

    static {
        EMPTY_CHANNEL = createChannel(
                UUID.fromString(EMPTY_UUID.getValue()),
                (long) EMPTY_TIME.getValue(),
                (long) EMPTY_TIME.getValue(),
                EMPTY_STRING.getValue()
        );
    }
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
        this.id       = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.name     = name;
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
    /** Returns: {@code EMPTY_CHANNEL} which is literally empty static Channel object */
    public static Channel createEmptyChannel() {
        return EMPTY_CHANNEL;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreateAt() {
        return createAt;
    }
    public Long getUpdateAt() {
        return updateAt;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel that = (Channel) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", name='" + name + '\'' +
                '}';
    }
}
