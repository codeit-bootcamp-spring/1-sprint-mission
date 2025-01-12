package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;

public class Message extends BaseEntity {
    private static final Message EMPTY_MESSAGE = new Message();

    private final String content;

    private Message() {
        this(EMPTY_BASE_ENTITY, EMPTY_STRING.getValue());
    }
    private Message(String content) {
        this.content = content;
    }
    private Message(BaseEntity baseEntity, String content) {
        super(baseEntity);
        this.content = content;
    }

    public static Message createMessage(String content) {
        return new Message(content);
    }
    public static Message createMessage(BaseEntity baseEntity, String content) {
        return new Message(baseEntity, content);
    }
    public static Message createEmptyMessage() {
        return EMPTY_MESSAGE;
    }

    public String getContent() {
        return content;
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
        return "Message{" +
                "content='" + content + '\'' +
                "} " + System.lineSeparator() +
                super.toString();
    }
}
