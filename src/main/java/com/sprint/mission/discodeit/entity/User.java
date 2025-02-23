package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;

@Getter
public class User extends BaseEntity implements  Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String password;
    private String userName;
    private String email;
    private UUID profileId;


    public User(String userName, String email, String password, UUID profileId) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.profileId = profileId;
    }

    public void update(String newUsername, String newEmail) {
        boolean isUpdated = false;
        if (!newUsername.equals(this.userName)) {
            this.userName = newUsername;
            isUpdated = true;
        }

        if (!newEmail.equals(this.email)) {
            this.email = newEmail;
            isUpdated = true;
        }

        if (isUpdated) {
            updated();
        }
    }

}
