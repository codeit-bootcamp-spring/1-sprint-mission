package com.sprint.mission.discodeit.entity;


public class User extends BaseEntity {
    private String nickname;
    private String username;
    private String email;
    private String phoneNumber;

    public User(String username, String email, String phoneNumber) {
        super();
        this.nickname = username;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void update(String nickname, String username, String email, String phoneNumber) {
        this.nickname = nickname;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        update();
    }
}
