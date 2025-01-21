package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.NotFoundException;
import lombok.Getter;
import lombok.ToString;

import java.util.*;


@Getter
@ToString
public class Channel {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;

    private String name;
    private Map<UUID, User> users;

    private Channel(String name) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = createdAt;
        this.name = name;
        users = new HashMap<>(100);
    }

    public static Channel from(String name) {
        return new Channel(name);
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

    public User getUser(UUID userId) {
        return Optional.ofNullable(users.get(userId))
                .orElseThrow(() -> new NotFoundException("현재 channel에 등록되지 않은 user입니다."));
    }
}