package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id = UUID.randomUUID();
    private final Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
    private String username;
    private String email;
    private String password;

    public User(Instant updatedAt, String username, String email, String password) {
        this.updatedAt = updatedAt;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void update(String newUsername, String newEmail, String newPassword) {
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
        }
        this.updatedAt = Instant.now();
    }
}
