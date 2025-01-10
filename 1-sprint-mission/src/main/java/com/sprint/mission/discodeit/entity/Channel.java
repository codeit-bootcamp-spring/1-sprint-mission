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

    public void updateName(String name) {
        this.name = name;
        setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Channel{id=" + getId() + ", name='" + name + "'}";
    }
}
