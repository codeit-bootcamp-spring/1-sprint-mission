package com.sprint.mission.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.security.UnresolvedPermission;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class Channel implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final String firstId;

    private String description;
    private String name;
    private final Instant createdAt;
    private Instant updatedAt;

    private final List<User> userList = new ArrayList<>();

    public Channel(String name, String description) {
        this.id = UUID.randomUUID();
        this.firstId = id.toString().split("-")[0];
        this.name = name;
        this.description = description;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // 양방향은 channel에서만
    public void addUser(User user){
        if (userList.contains(user)){
            userList.add(user);
            user.getChannels().add(this);
        }
    }
}