package sprint.mission.discodeit.entity;

import java.util.Objects;

public class Channel {
    private final Common common;
    private final String name;

    private Channel(Common common) {
        this(common, "");
    }
    private Channel(String name) {
        this(Common.createCommon(), name);
    }
    private Channel(Common common, String name) {
        this.common = common;
        this.name   = name;
    }

    public static Channel createChannel(String name) {
        return new Channel(name);
    }
    public static Channel createChannel(Common common, String name) {
        return new Channel(common, name);
    }
    public static Channel createEmptyChannel() {
        return new Channel(Common.createEmptyCommon());
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
        Channel channel = (Channel) o;
        return Objects.equals(common, channel.common) && Objects.equals(name, channel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(common, name);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "common=" + common +
                ", name='" + name + '\'' +
                '}';
    }
}
