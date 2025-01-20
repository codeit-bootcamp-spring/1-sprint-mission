package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private final long createdAt;
    private final List<Channel> channelList; // 참가중인 채널
    private long updatedAt;
    private String name;
    private String email;

    public User(String name, String email) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        channelList = new ArrayList<>();
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }

    private void updateUpdatedAt() {
        updatedAt = System.currentTimeMillis();
    }

    public void updateName(String name) {
        this.name = name;
        updateUpdatedAt();
    }

    public void updateEmail(String email) {
        this.email = email;
        updateUpdatedAt();
    }

    public void addChannel(Channel newChannel) {
        channelList.add(newChannel);
    }

    public void removeChannel(Channel removeChannel) {
        channelList.remove(removeChannel);
    }

    @Override
    public String toString() {
        return "User{name:" + name + ",email:" + email + ",channelList:" + channelList + ",createdAt:" + createdAt + ",updateAt:" + updatedAt + "}";
    }
}
