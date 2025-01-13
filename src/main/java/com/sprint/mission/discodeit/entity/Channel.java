package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private UUID id;
    private String channelName;
    private String description;
    private long createdAt;
    private long updatedAt;

    public Channel(UUID id, String channelName, String description, long createdAt) {
        this.id = id;
        this.channelName = channelName;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }
    public String getChannelName() {
        return channelName;
    }
    public String getDescription() {
        return description;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();
    }
    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    public String toString() {
        return "Channel {" +
                "id = " + id +
                ", channelName = " + channelName +
                ", description = " + description +
                ", createdAt = " + createdAt +
                ", updatedAt = " + updatedAt + "}";
    }

}