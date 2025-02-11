package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class User extends BaseEntity implements  Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private transient final String password;
    private String userName;
    private String email;

    public User(String userName, String email, String password) {
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
        update();
    }

    public void updateUserEmail(String email) {
        this.email = email;
        update();
    }

    public void display() {
        System.out.println("사용자 이름: " + userName + "\n이메일: " + email);
    }

}
