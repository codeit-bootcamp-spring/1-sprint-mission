package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    // 공통 필드
    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    // User 전용 필드
    private String username;
    private String email;

    // 생성자
    public User(String username, String email) {
        this.id = UUID.randomUUID();         // UUID 자동 생성
        this.createdAt = System.currentTimeMillis(); // 현재 시간 (밀리초)
        this.updatedAt = this.createdAt;     // 최초 생성 시점으로 초기값 설정

        // 나머지는 파라미터로 초기화
        this.username = username;
        this.email = email;
    }

    // Getter 메서드들
    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // Update 메서드 예시
    public void updateUsername(String newUsername) {
        this.username = newUsername;
        this.updatedAt = System.currentTimeMillis(); // 업데이트 시간 갱신
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
        this.updatedAt = System.currentTimeMillis();
    }
}
