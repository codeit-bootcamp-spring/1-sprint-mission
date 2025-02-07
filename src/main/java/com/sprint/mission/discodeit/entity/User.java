package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String loginId;
    private String password;

    private User(String name, String loginId, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }

    public static User of(String name, String loginId, String password) {
        return new User(name, loginId, password);
    }

    public void updateLoginId(String loginId) {
        this.loginId = loginId;
        updatedAt = Instant.now();
    }

    public void updatePassword(String password) {
        this.password = password;
        updatedAt = Instant.now();
    }

    public void updateName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }
}
