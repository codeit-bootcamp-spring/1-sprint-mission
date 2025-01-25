package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

public class User extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID userUuid;
    private final Long createdAt;
    private Long updatedAt;
    private String userName;
    private String userId;


    public User(String userName, String userId){

        userUuid = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.userName = userName;
        this.userId = userId;
    }

    //Getter


    public String getUserUuid() {
        return userUuid.toString();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }



    //Updated 메소드
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
