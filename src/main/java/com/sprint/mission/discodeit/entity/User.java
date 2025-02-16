package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String username;
    private String password;
    private String email;
    private final Instant createdAt;
    private Instant updatedAt;

    public User(String username, String password, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = Instant.now();
    }

    public void updateName(String username) {
        this.username = username;
        this.updatedAt = Instant.now();
    }
    public void updatePassword(String password) {
        this.password = password;
        this.updatedAt = Instant.now();
    }
    public void updateEmail(String email) {
        this.email = email;
        this.updatedAt = Instant.now();
    }

    public String toString() {
        return "User {" +
                "id=" + id +
                ", username = " + username +
                ", password = " + password +
                ", email = " + email +
                ", createdAt = " + createdAt +
                ", updatedAt = " + updatedAt + "}";
    }

}