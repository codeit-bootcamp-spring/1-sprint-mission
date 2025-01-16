package com.sprint.mission.entity;

import java.util.UUID;

public class User {

    private long updateAt;
    private final UUID id;
    private final long createdAt;
    private String email;


    public User (UUID id, String email){
        this.id = id;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
        this.updateAt = this.createdAt;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public UUID getID(){
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }
}
