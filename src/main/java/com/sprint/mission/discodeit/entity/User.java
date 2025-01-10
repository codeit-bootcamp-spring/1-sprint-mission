package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.global.UtilMethod;

import java.util.List;
import java.util.UUID;

public class User extends BaseEntity {
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String profileImageUrl;
    private boolean active;

    public User(String name, String nickname, String email, String password,
                String profileImageUrl, boolean active) {
        super();
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.active = active;
    }


    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public boolean isActive() {
        return active;
    }


    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateActive(){
        this.active = !this.active;
    }

}
