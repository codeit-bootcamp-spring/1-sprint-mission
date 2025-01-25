package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String username;
    private transient String password;
    private String email;
    private long createdAt;
    private long updatedAt;

    public User(String username, String password, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateName(String username) {
        this.username = username;
        this.updatedAt = System.currentTimeMillis();
    }
    public void updatePassword(String password) {
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }
    public void updateEmail(String email) {
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
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