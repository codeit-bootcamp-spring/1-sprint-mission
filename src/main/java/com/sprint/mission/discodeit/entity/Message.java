package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    public Message(UUID id, Long createdAt){
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
        return "Message{id=" + id + ", createdAt='" + createdAt + "'}";
    }
}
