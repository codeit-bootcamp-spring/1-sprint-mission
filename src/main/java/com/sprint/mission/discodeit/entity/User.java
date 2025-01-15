package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;

    // 생성자
    public User(String name, String email) {
        super();
        this.name = name;
        this.email = email;
    }

    // Getter
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // 업데이트 메서드
    public void updateName(String name) {
        this.name = name;
        update();
    }

    public void updateEmail(String email) {
        this.email = email;
        update();
    }

    @Override
    public String toString() {
        return "User {id='" + getId() + "', name='" + name + "', email='" + email + "'}";
    }

}
