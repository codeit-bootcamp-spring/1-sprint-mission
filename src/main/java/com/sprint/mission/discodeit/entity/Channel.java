package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.NotFoundException;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;


@Getter
@ToString
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private Map<UUID, User> users;

    private Channel(String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.name = name;
        this.description = description;
        users = new HashMap<>(100);
    }

    public static Channel of(String name, String description) {
        return new Channel(name, description);
    }

    public void updateName(String name) {
        this.name = name;
        updatedAt = Instant.now();
    }

    public void updateDescription(String description) {
        this.description = description;
        updatedAt = Instant.now();
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
        updatedAt = Instant.now();
    }

    public void deleteUser(UUID id) {
        users.remove(id);
        updatedAt = Instant.now();
    }

    public User getUser(UUID userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundException("현재 channel에 등록되지 않은 user입니다."));
    }
}