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
    private String email;
    private String password;

    private User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User of(String name, String email, String password) {
        return new User(name, email, password);
    }

    public void updateEmail(String email) {
        this.email = email;
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
