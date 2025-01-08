package discodeit.entity;

public class Message {
    private final Common common;

    private Message() {
        common = Common.createCommon();
    }

    public static Message createMessage() {
        return new Message();
    }

    public Common getCommon() {
        return common;
    }
}
