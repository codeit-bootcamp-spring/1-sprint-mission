package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID userId;
    private Instant createdAt;
    private Instant updatedAt;
    private String username;
    private String userEmail;
    private String password;
    private UUID profileId;

    public User(String username, String userEmail, String password, UUID profileId) {
        this.userId = UUID.randomUUID();
        this.username = username;
        this.userEmail = userEmail;
        this.password = password;
        this.profileId = profileId;
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
    }

    public void update(String newUsername, String newEmail, String newPassword, UUID newProfileId) {
        boolean anyValueUpdated = false;
        if (newUsername != null && !newUsername.equals(this.username)) {
            this.username = newUsername;
            anyValueUpdated = true;
        }
        if (newEmail != null && !newEmail.equals(this.userEmail)) {
            this.userEmail = newEmail;
            anyValueUpdated = true;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
            anyValueUpdated = true;
        }
        if (newProfileId != null && !newProfileId.equals(this.profileId)) {
            this.profileId = newProfileId;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
