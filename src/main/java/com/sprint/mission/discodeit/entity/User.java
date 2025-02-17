package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor // 모든 필드를 포함한 생성자 생성
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();  // Instant로 변경
    private Instant updatedAt;  // Instant로 변경
    private String username;
    private String email;
    private String password;

    // 새로 추가된 생성자
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.updatedAt = Instant.now();  // 생성시 updatedAt 설정
    }

    // update 메서드에서 updatedAt 갱신
    public void update(String newUsername, String newEmail, String newPassword) {
        boolean anyValueUpdated = false;
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
            anyValueUpdated = true;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
            anyValueUpdated = true;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();  // 수정 시점에 updatedAt 갱신
        }
    }
}
