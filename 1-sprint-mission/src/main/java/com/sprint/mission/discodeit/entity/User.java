package com.sprint.mission.discodeit.entity;

import java.util.*;

public class User {

    private final UUID userUuid;
    private final long createdAt;
    private long updatedAt;
    private String userName;
    private String userId;
    private final List<User>userData;

    public User(String userName, String userId){
        userData = new ArrayList<>();
        userUuid = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.userName = userName;
        this.userId = userId;
    }

    //Getter
    public List<User> getUserData() {
        return userData;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }


    //Updated 메소드
    public void updateUserId(String userId) {
        this.userId = userId;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public void updateUpdatedAt() {
        this.updatedAt = System.currentTimeMillis();
    }
}
