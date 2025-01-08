package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private long createdAt;
    private long updatedAt;
    private String name;
    private List<Channel> channelList; // 참가중인 채널

    public User(String name) {
        id = UUID.randomUUID();
        createdAt = System.currentTimeMillis();
        this.name = name;
        channelList = new ArrayList<>();
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

    public List<Channel> getChannelList() {
        return channelList;
    }

    // update
    public void updateName(String name) {
        this.name = name;
    }

    public void addChannal(Channel newChannel) {
        channelList.add(newChannel);
    }

    public void removeChannel(Channel removeChannel) {
        channelList.remove(removeChannel);
    }

}
