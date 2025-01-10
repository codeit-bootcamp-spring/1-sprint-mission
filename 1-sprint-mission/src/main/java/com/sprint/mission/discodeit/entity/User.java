package com.sprint.mission.discodeit.entity;

import java.util.*;
import java.util.UUID;

public class User{
    private UUID id = UUID.randomUUID();
    private String name = "Undefined";
    private final long createdAt = System.currentTimeMillis();
    private long updatedAt = System.currentTimeMillis();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatedAt() {
        return createdAt;
    }


    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

}
