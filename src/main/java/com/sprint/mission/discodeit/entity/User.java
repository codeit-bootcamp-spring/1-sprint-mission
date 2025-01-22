package com.sprint.mission.discodeit.entity;

import java.util.UUID;

import static com.sprint.mission.discodeit.entity.security.Encryptor.getEncryptedPassword;

public class User {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String password;
    private String name;


    public User(String password, String name) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = null;
        this.password = getEncryptedPassword(password);
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public void updatePassword(String password) {
        this.password = getEncryptedPassword(password);
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

}
