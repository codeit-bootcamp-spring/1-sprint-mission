package sprint.mission.discodeit.entity;

import java.util.Objects;

public class Message {
    private static final Message EMPTY_MESSAGE =
            new Message(BaseEntity.createEmptyCommon());

    private final BaseEntity baseEntity;
    private final String     content;

    private Message(BaseEntity baseEntity) {
        this(baseEntity, "");
    }
    private Message(String content) {
        this(BaseEntity.createCommon(), content);
    }
    private Message(BaseEntity baseEntity, String content) {
        this.baseEntity = baseEntity;
        this.content    = content;
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

    public BaseEntity getCommon() {
        return baseEntity;
    }
    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(baseEntity, message.baseEntity) && Objects.equals(content, message.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseEntity, content);
    }

    @Override
    public String toString() {
        return "Message{" +
                "common=" + baseEntity +
                ", name='" + content + '\'' +
                '}';
    }
}
