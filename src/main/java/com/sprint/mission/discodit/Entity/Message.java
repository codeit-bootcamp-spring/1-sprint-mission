package com.sprint.mission.discodit.Entity;

import java.util.UUID;

public class Message {
    UUID id;
    Long createdAt, updatedAt;
    public Message(UUID id) {
        this.id = id;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                '}';
    }
}
