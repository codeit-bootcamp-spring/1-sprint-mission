package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel{
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String channelName;

    public Channel(UUID id, Long createdAt, Long updatedAt, String channelName) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.channelName = channelName;
    }

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getChannelName() {
        return channelName;
    }

    public void update(String channelName) {
        this.channelName = channelName;
        this.updatedAt = System.currentTimeMillis();
    }
}
