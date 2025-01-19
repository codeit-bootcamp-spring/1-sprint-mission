package com.sprint.mission.discodeit.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final long createdAt;
    private long updatedAt;
    private String name;
    private UUID ownerId;
    private Set<UUID> users;

    public Channel(String name, User owner) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.name = name;

        this.ownerId = owner.getId();
        this.users = new HashSet<>();
        this.users.add(owner.getId());
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public Set<UUID> getUsers() {
        return new HashSet<>(users); // Return a copy to prevent external modification
    }

    public void updateName(String newName) {
        this.name = newName;
        this.updatedAt = System.currentTimeMillis();
    }

    public void addUser(UUID userId) {
        this.users.add(userId);
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeUser(UUID userId) {
        this.users.remove(userId);
        this.updatedAt = System.currentTimeMillis();
    }
}
