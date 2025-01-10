package com.sprint.mission.discodeit.entity;


public class Channel extends BaseEntity {
    private String name;

    public Channel(String name) {
        super();
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

