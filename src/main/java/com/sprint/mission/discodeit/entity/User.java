package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User extends BaseEntity implements  Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient final String password;
    private String userName;
    private String email;

    @Builder
    public User(String userName, String email, String password) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void update(String newUsername, String newEmail) {
        boolean anyValueUpdated = false;
        if (newUsername != null && !newUsername.equals(this.userName)) {
            this.userName = newUsername;
            anyValueUpdated = true;
        }
        if (newEmail != null && !newEmail.equals(this.email)) {
            this.email = newEmail;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            updated();
        }
    }


}
