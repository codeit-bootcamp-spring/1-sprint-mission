package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;

@Getter
public class User extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 13L;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void update(String name, String email, String password) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (password != null) this.password = password;
        updateTimeStamp();
    }
}

