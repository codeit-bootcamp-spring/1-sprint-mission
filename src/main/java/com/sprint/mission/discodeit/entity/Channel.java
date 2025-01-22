package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private String channelName;
    private final List<Message> messages;
    private final long createdAt;
    private long updatedAt;

    public Channel(String channelName) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.messages = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void updateChannelName(String newChannelName) {
        this.channelName = newChannelName;
        this.updatedAt = System.currentTimeMillis();
    }

    public void addMessage(Message message) {
        messages.add(message);
        this.updatedAt = System.currentTimeMillis();
    }
}
