package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {

    private String userid;
    private String password;
    private String username;
    private String email;
    public User(String userid, String password, String username, String email) {
        this.userid = userid;
        this.password = password;
        this.username = username;
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void updateUsername(String username) {
        this.username = username;
        setUpdatedAt(System.currentTimeMillis());
    }
    public void updateUserEmail(String email) {
        this.email = email;
        setUpdatedAt(System.currentTimeMillis());
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
