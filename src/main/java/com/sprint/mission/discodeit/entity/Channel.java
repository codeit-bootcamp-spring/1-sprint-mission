package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.util.UUID;

public class Channel extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    public Channel(UUID id, ChannelType aPublic, String name, String s) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        update();
    }

    @Override
    public String toString() {
        return "Channel{id=" + id + ", name='" + name + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}