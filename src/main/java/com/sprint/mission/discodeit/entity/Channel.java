package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String name;        // 채널 이름
    private String description; // 채널 설명

    public Channel(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
        setUpdateAT(System.currentTimeMillis());
    }

    public String getDescription() {
        return description;
    }

    public void updateDescription(String description) {
        this.description = description;
        setUpdateAT(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdateAT() +
                '}';
    }

}
