package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID

    private String name;
    private String description;
    private User creator;

    public Channel(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public Channel(String name, String description, User creator) {
        super();
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    public String getName() { return name; }
    public void updateName(String name) {
        this.name = name;
        setUpdateAT(System.currentTimeMillis());
    }

    public String getDescription() { return description; }
    public void updateDescription(String description) {
        this.description = description;
        setUpdateAT(System.currentTimeMillis());
    }

    public User getCreator() { return creator; }
    public void updateCreator(User creator) {
        this.creator = creator;
        setUpdateAT(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", creator=" + (creator != null ? creator.getUsername() : "Unknown") +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdateAT() +
                '}';
    }
}
