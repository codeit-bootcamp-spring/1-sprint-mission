package discodeit.entity;

public class Channel {
    private final Common common;

    private Channel() {
        common = Common.createCommon();
    }

    public static Channel createChannel() {
        return new Channel();
    }

    public Common getCommon() {
        return common;
    }
}
