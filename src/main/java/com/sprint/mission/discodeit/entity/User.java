package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    UUID id;
    long createdAt;
    long updatedAt;
    String userName;

    public User(UUID id, String userName){
        this.id = id;
        this.userName = userName;
        this.createdAt = System.currentTimeMillis();

    }

    public UUID getId(){

        return id;
    }

    public long getCreateAt(){

        return createdAt;
    }

    public long getUpdateAt(){

        return updatedAt;
    }

    public String getUserName(){

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

