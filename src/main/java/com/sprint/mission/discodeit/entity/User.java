package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.util.UUID;

public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    public User(UUID id, String username, String mail, String woody1234) {
        super();
        this.id = id;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        update();
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + '\'' + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + '}';
    }
}