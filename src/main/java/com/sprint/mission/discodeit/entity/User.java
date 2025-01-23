package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String username;
    private String password;

    public User(String username, String password) {
        // 유효성 검사 추가
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("사용자 이름이 비어있습니다.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 비어있습니다.");
        }
        if (password.length() < 10) {
            throw new IllegalArgumentException("비밀번호는 10자리 이상이어야 합니다.");
        }

        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.username = username;
        this.password = password;
    }

    // 각 필드의 Getter 함수 정의
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public long getCreatedAt() { return createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public String getPassword() { return password; }

    public void updateUsername(String newUsername) {
        // 업데이트 시에도 유효성 검사 추가
        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("새 사용자 이름이 비어있습니다.");
        }
        this.username = newUsername;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updatePassword(String newPassword) {
        // 업데이트 시에도 유효성 검사 추가
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("새 비밀번호가 비어 있습니다.");
        }
        if (newPassword.length() < 10) {
            throw new IllegalArgumentException("새 비밀번호는 10자리 이상이어야 합니다.");
        }
        this.password = newPassword;
        this.updatedAt = System.currentTimeMillis();
    }
}
