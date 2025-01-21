package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class User {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String name;

    public User(String name){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }

    public void updateId(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void updateName(String name){
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }



    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return name + "/ createdAt = " + simpleDateFormat.format(createdAt) + "/ updatedAt=" + simpleDateFormat.format(updatedAt);

    }
}
