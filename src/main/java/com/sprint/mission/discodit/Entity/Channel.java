package com.sprint.mission.discodit.Entity;

import java.util.UUID;

public class Channel {
    UUID id;
    Long createdAt, updatedAt;
    public Channel(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }
}
