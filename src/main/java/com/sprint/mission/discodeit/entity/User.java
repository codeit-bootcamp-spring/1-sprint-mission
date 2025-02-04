package com.sprint.mission.discodeit.entity;

import java.io.Serial;

public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 3L;
    private String name;
    private String email;
    private String password;
    private UserStatus status;

    public User(String name, String email, String password, UserStatus status) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public void update(String name, String email, String password, UserStatus status) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (password != null) this.password = password;
        if (status != null) this.status = status;
        updateTimeStamp();
    }
}

