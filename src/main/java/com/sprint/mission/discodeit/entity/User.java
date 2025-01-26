package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class User extends BaseEntity implements Serializable {
   private String userName;
   private String userEmail;

    public User(String userName, String userEmail) {
        super();
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
        setUpdatedAt();
    }

    public void updateUserEmail(String userEmail) {
        this.userEmail = userEmail;
        setUpdatedAt();
    }


    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}
