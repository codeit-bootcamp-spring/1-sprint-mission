package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String name;
    private List<User> members;
    private List<Message> messages;

    public Channel(String name, List<User> members) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = null;
        this.name = name;
        this.members = members;
        this.messages = new ArrayList<>();
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

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void updateName(String name) {
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateMembers(List<User> members) {
        this.members = members;
        this.updatedAt = System.currentTimeMillis();
    }

    public void updateMessages(List<Message> messages) {
        this.messages = messages;
        this.updatedAt = System.currentTimeMillis();
    }

    public void addMessage(Message message) {
        this.messages.add(message);
        this.updatedAt = System.currentTimeMillis();
    }
}
