package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String alias;
    private String email;
    private String password;
    private UUID profileId;

    public User(String alias, String email, String password, UUID profileId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.alias = alias;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void update(String newAlias, String newEmail, String newPassword, UUID newProfileId) {
        boolean flag = false;
        if (newAlias != null && !newAlias.equals(this.alias)) {
            this.alias = newAlias;
            flag = true;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
            flag = true;
        }
        if (newPassword != null && !newPassword.equals(this.password)) {
            this.password = newPassword;
            flag = true;
        }
        if (newProfileId != null && !newProfileId.equals(this.profileId)) {
            this.profileId = newProfileId;
            flag = true;
        }

        if (flag) {
            this.updatedAt = Instant.now();
        }
    }
}
