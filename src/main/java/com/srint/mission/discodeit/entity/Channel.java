package com.srint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {

    private final UUID id;
    private final long createdAt;
    private long updatedAt;

    private String name;
    private String description;
    private ChannelType type;

    public Channel(String name, String description, ChannelType type) {

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().getEpochSecond();

        this.name = name;
        this.description = description;
        this.type = type;
    }

    public static boolean validation(String name, String description){
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (description == null || description.isEmpty()) {
            return false;
        }
        return true;
    }

    public UUID getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ChannelType getType() {
        return type;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now().getEpochSecond();
    }

    public void setName(String name) {
        if(name ==null && name.equals(this.name)){
            throw new IllegalArgumentException("입력한 값이 null 혹은 중복입니다.");
        }
        this.name = name;
        setUpdatedAt();
    }

    public void setDescription(String description) {
        if(description ==null && description.equals(this.description)){
            throw new IllegalArgumentException("입력한 값이 null 혹은 중복입니다.");
        }
        this.description = description;
        setUpdatedAt();
    }

    public void setType(ChannelType type) {
        this.type = type;
        setUpdatedAt();
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                '}';
    }
}