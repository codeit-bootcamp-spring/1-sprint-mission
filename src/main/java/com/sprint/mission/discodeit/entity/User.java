package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;


@Getter
@ToString
public class User {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String name;
    private String loginId;
    private String password;

    private User(String name, String loginId, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
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
        this.updatedAt = System.currentTimeMillis();
    }

    public void updatePassword(String password) {
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }
}
