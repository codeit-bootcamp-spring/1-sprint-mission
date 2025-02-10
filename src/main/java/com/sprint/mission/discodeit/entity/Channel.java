package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel implements Serializable {

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType type;
    private List<UUID> userIds;

    public Channel(String name, String description, ChannelType type) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.name = name;
        this.description = description;
        this.type = type;
        userIds = new ArrayList<>();
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
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

    public List<UUID> getUserIds() {
        return userIds;
    }

    public void addUsers(UUID userId) {
        userIds.add(userId);
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }

    public void setChannel(String name, String description){
        if(name !=null && !name.equals(this.name)){
            this.name = name;
        }else{
            throw new IllegalArgumentException("입력한 채널이름: "+name+"이 기존 값과 같습니다.");
        }

        if(description !=null && !description.equals(this.description)){
            this.description = description;
        } else{
            throw new IllegalArgumentException("입력한 설명: "+description+"이 기존 값과 같습니다.");
        }
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