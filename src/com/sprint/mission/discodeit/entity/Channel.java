package com.sprint.mission.discodeit.entity;

import static com.sprint.mission.discodeit.constant.StringConstant.EMPTY_STRING;

public class Channel extends BaseEntity {
    private static final Channel EMPTY_CHANNEL = new Channel();

    private final String name;

    private Channel() {
        this(EMPTY_BASE_ENTITY, EMPTY_STRING.getValue());
    }
    private Channel(String name) {
        this.name = name;
    }
    private Channel(BaseEntity baseEntity, String name) {
        super(baseEntity);
        this.name = name;
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

    public String getName() {
        return name;
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
        return "Channel{" +
                "name='" + name + '\'' +
                "} " + System.lineSeparator() +
                super.toString();
    }
}
