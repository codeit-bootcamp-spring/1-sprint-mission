package com.sprint.mission.discodeit.entity;

import java.text.SimpleDateFormat;
import java.util.*;

public class Channel{
    private UUID channelId = UUID.randomUUID();
    private String name;
    private final long createdAt = System.currentTimeMillis();
    private long updatedAt = System.currentTimeMillis();
    private final Map<UUID, String> users = new HashMap<>();

    public UUID getChannelId() {
        return channelId;
    }

    public void setChannelId(UUID id){ this.channelId = id;}

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

    public Map<UUID, String> getUsers() {
        return users;
    }

    public void setUsers(UUID userId, String name){
        users.put(userId,name);
    }

}
