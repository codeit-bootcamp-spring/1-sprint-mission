package sprint.mission.discodeit.entity;

import java.util.Objects;

public class Message {
    private static final Message EMPTY_MESSAGE =
            new Message(Common.createEmptyCommon());

    private final Common common;
    private final String name;

    private Message() {
        this(Common.createCommon(), "");
    }
    private Message(Common common) {
        this(common, "");
    }
    private Message(String name) {
        this(Common.createCommon(), name);
    }
    private Message(Common common, String name) {
        this.common = common;
        this.name   = name;
    }

    public static Message createMessage(String name) {
        return new Message(name);
    }
    public static Message createMessage(Common common, String name) {
        return new Message(common, name);
    }
    public static Message createEmptyMessage() {
        return EMPTY_MESSAGE;
    }

    public Common getCommon() {
        return common;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(common, message.common) && Objects.equals(name, message.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(common, name);
    }

    @Override
    public String toString() {
        return "Message{" +
                "common=" + common +
                ", name='" + name + '\'' +
                '}';
    }
}
