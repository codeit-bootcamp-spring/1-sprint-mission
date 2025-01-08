package discodeit.entity;

import java.util.Objects;

public class Channel {
    private final Common common;

    private Channel() {
        common = Common.createCommon();
    }

    public static Channel createChannel() {
        return new Channel();
    }
    public static Channel createEmptyChannel() {
        return new Channel();
    }

    public Common getCommon() {
        return common;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(common, channel.common);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(common);
    }
}
