package com.sprint.mission.entity;

import java.util.UUID;

public class User {

    private long updateAt;
    private final UUID id;
    private final long createdAt;
    private String email;
    private String name;


    public User (UUID id, String email, String name){
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = System.currentTimeMillis();
        this.updateAt = this.createdAt;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
