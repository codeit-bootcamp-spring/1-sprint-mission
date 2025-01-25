package com.sprint.mission.discodeit.entity;

import java.util.*;

public class User extends BaseEntity{

    private final UUID userUuid;
    private final Long createdAt;
    private Long updatedAt;
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



    //Updated 메소드
    public void updateUserId(String userId) {
        this.userId = userId;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }


}
