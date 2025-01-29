package com.sprint.mission.discodeit.entity;

import java.io.Serial;

public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3L;
    private String name;
    private String email;
    private UserStatus status;

    public User(String name, String email, UserStatus status) {
        super();
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void update(String name, String email, UserStatus status) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (status != null) this.status = status;
        updateTimeStamp();
    }
}

