package com.sprint.mission.discodit.Entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private String userName;
    private final long createdAt;
    private long updatedAt;

    public User(String userName) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.userName = userName;
    }

    public void update(String userName) {
        this.userName = userName;
        updatedAt = System.currentTimeMillis();
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getUserId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", userName='" + userName + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}
