package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private String email;
    private transient String password;

    public static User createUser(String name, String email, String password) {
        return new User(name, email, password);
    }

    private User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void update(String newName, String newEmail, String newPassword) {
        boolean isChanged = false;
        if (!newName.equals(this.name)) {
            this.name = newName;
            isChanged = true;
        }
        if (!newEmail.equals(this.email)) {
            this.email = newEmail;
            isChanged = true;
        }
        if (!newPassword.equals(this.password)) {
            this.password = newPassword;
            isChanged = true;
        }

        if (isChanged) {
            updatedAt = System.currentTimeMillis();
        }
    }

    @Override
    public String toString() {
        return "User{id:" + id + ",name:" + name + ",email:" + email + ",createdAt:" + createdAt + ",updateAt:" + updatedAt + "}";
    }
}
