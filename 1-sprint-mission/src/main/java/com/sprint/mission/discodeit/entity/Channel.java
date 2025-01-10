package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private final List<Message> messages;

    public Channel(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.name = name;
        this.messages = new ArrayList<>();
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

    public void update(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "[id=" + id + ", name=" + name + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}
