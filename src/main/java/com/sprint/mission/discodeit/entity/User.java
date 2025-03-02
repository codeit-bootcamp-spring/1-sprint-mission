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
    private String email;
    private String password;
    private final Instant createdAt;
    private Instant updatedAt;
    private UUID profileId;

    public User(String username, String email, String password,  UUID profileId) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = Instant.now();
        this.profileId = profileId;

    }

    public void updateUsername(String newUsername) {
        this.username = newUsername;
        this.updatedAt = Instant.now();
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
        this.updatedAt = Instant.now();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = Instant.now();
    }

    public void updateProfileId(UUID newProfileId) {
        this.profileId = newProfileId;
        this.updatedAt = Instant.now();
    }

}
