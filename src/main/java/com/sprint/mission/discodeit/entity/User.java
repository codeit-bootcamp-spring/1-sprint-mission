package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String name;

    public User(UUID id, Long createdAt, Long updatedAt, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public void update(String name, Long updatedAt) {
        this.name = name;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}
