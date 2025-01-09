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
    }
    public void updateUserEmail(String email) {
        this.email = email;
    }

    public void updateUserid(String userid) {
        this.userid = userid;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
