package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Channel extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID

    private String name;        // 채널 이름
    private String description; // 채널 설명
    private User creator;       // 채널 생성자 (추가된 필드)

    // 기존 생성자
    public Channel(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    // 새로운 생성자 (채널 생성자를 포함)
    public Channel(String name, String description, User creator) {
        super();
        this.name = name;
        this.description = description;
        this.creator = creator;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void updateName(String name) {
        this.name = name;
        setUpdateAT(System.currentTimeMillis());
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void updateDescription(String description) {
        this.description = description;
        setUpdateAT(System.currentTimeMillis());
    }

    // Getter for creator
    public User getCreator() {
        return creator;
    }

    // Setter for creator
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
