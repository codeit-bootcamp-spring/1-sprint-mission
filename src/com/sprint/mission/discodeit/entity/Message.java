package com.sprint.mission.discodeit.entity;

import java.util.Objects;
import java.util.UUID;

import static com.sprint.mission.discodeit.constant.IntegerConstant.EMPTY_TIME;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;
import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_UUID;

public class Message {
    public static final Message EMPTY_MESSAGE;
    private final UUID   id;
    private final Long   createAt;
    private final Long   updateAt;
    private final String content;

    static {
        EMPTY_MESSAGE = createMessage(
                UUID.fromString(EMPTY_UUID.getValue()),
                (long) EMPTY_TIME.getValue(),
                (long) EMPTY_TIME.getValue(),
                EMPTY_STRING.getValue()
        );
    }
    private Message(String content) {
        this(
                UUID.randomUUID(),
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                content
        );
    }
    private Message(UUID id, String content) {
        this(
                id,
                System.currentTimeMillis(),
                System.currentTimeMillis(),
                content
        );
    }
    private Message(UUID id, Long createAt, String content) {
        this(
                id,
                createAt,
                System.currentTimeMillis(),
                content
        );
    }
    private Message(UUID id, Long createAt, Long updateAt, String content) {
        this.id       = id;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.content  = content;
    }

    public static Message createMessage(String content) {
        return new Message(content);
    }
    public static Message createMessage(UUID id, String content) {
        return new Message(id, content);
    }
    public static Message createMessage(UUID id, Long createAt, String content) {
        return new Message(id, createAt, content);
    }
    public static Message createMessage(UUID id, Long createAt, Long updateAt, String content) {
        return new Message(id, createAt, updateAt, content);
    }
    public static Message createEmptyMessage() {
        return EMPTY_MESSAGE;
    }

    public UUID   getId() {
        return id;
    }
    public Long   getCreateAt() {
        return createAt;
    }
    public Long   getUpdateAt() {
        return updateAt;
    }
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message that = (Message) o;
        return Objects.equals(id, that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", content='" + content + '\'' +
                '}';
    }
}
