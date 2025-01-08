package discodeit.entity;

import java.util.Objects;

public class Message {
    private final Common common;

    private Message() {
        common = Common.createCommon();
    }

    public static Message createMessage() {
        return new Message();
    }
    public static Message createEmptyMessage() {
        return new Message();
    }

    public Common getCommon() {
        return common;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(common, message.common);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(common);
    }
}
