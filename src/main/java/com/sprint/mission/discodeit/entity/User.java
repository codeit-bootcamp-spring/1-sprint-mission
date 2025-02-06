package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String username;
    private String email;
    private String phoneNumber;
    private String password;

    public User() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public User(String username, String email, String phoneNumber, String password) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public void update(String username, String email, String phoneNumber, String password) {
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        updatedAt = Instant.now();
    }
}
