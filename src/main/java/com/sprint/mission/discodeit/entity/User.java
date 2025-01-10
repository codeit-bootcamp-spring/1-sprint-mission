package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String username;
    private String password;

    public User(String username, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.username = username;
        this.password = password;
    }

    // 각 필드의 Getter 함수 정의
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    // 현재는 사용하지 않는 값이지만, 확장성을 위해 모든 필드에 대해 Getter 함수 작성
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public String getPassword() { return password; }

    public void updateUsername(String newUsername) {
        this.username = newUsername;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = System.currentTimeMillis();
    }
}
