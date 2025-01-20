package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID

    private String username;  // 사용자 이름
    private String email;     // 사용자 이메일

    public User(String username, String email) {
        super();
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }

    public void updateUsername(String username) {
        this.username = username;
        setUpdateAT(System.currentTimeMillis());
    }

    public void updateEmail(String email) {
        this.email = email;
        setUpdateAT(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdateAT() +
                '}';
    }
}
