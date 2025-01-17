package com.sprint.mission.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.util.UUID;
import java.time.format.DateTimeFormatter;

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

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(Instant.ofEpochMilli(createdAt));
    }

    @Override
    public String toString() {
        return  getName() +
                "\n이메일 : " + email  +
                "\n생성 시간 : " + getFormattedCreatedAt();
    }
}
