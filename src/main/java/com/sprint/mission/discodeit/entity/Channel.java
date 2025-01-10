package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public Channel(UUID id, Long createdAt){
        this.id = id;
        this.createdAt = createdAt;
    }

    public UUID GetId() {
        return id;
    }
    public Long GetCreatedAt() {
        return createdAt;
    }
    public void UpdateId(UUID id) {
        this.id = id;
    }
    public void UpdateCreatedAt(Long CreatedAt) {
        this.createdAt = createdAt;
    }

    public String toString() {
        return "Channel{id=" + id + ", createdAt='" + createdAt + "'}";
    }
}
