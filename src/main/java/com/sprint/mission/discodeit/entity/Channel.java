package com.sprint.mission.discodeit.entity;

import java.util.*;

public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String name;
    private Map<UUID, User> users;

    public Channel(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.name = name;
        users = new HashMap<>();
    }

    public void updateName(String name) {
        this.name = name;
        updatedAt = System.currentTimeMillis();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        updatedAt = System.currentTimeMillis();
    }

    public void deleteUser(UUID id) {
        users.remove(id);
        updatedAt = System.currentTimeMillis();
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

    public User getUser(UUID userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new RuntimeException("현재 channel에 등록되지 않은 user입니다.");
        }
        return user;
    }

    public Map<UUID, User> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id.toString().substring(0, 8) +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", name='" + name + '\'' +
                ",\n                users=" + users +
                '}';
    }
}