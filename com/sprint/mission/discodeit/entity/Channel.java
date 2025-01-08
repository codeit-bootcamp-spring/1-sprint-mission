package sprint.mission.discodeit.entity;

import java.util.Objects;

public class Channel {
    private static final Channel EMPTY_CHANNEL =
            new Channel(BaseEntity.createEmptyCommon());

    private final BaseEntity baseEntity;
    private final String     name;

    private Channel(BaseEntity baseEntity) {
        this(baseEntity, "");
    }
    private Channel(String name) {
        this(BaseEntity.createCommon(), name);
    }
    private Channel(BaseEntity baseEntity, String name) {
        this.baseEntity = baseEntity;
        this.name   = name;
    }

    public static Channel createChannel(String name) {
        return new Channel(name);
    }
    public static Channel createChannel(BaseEntity baseEntity, String name) {
        return new Channel(baseEntity, name);
    }
    public static Channel createEmptyChannel() {
        return EMPTY_CHANNEL;
    }

    public BaseEntity getCommon() {
        return baseEntity;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Channel channel = (Channel) o;
        return Objects.equals(baseEntity, channel.baseEntity) && Objects.equals(name, channel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseEntity, name);
    }

    @Override
    public String toString() {
        return "Channel{" +
                "common=" + baseEntity +
                ", name='" + name + '\'' +
                '}';
    }
}
