package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable {
    private String id;
    private Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
        this.updatedAt = Instant.now();
    }
}