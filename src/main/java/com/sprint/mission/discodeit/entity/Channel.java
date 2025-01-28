package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// //JCF 테스트 시
//public class Channel {


// //File 테스트시
 public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private String channelName;
    private final List<Message> messages;
    private final List<UUID> userIds; //+
    private final long createdAt;
    private long updatedAt;

    public Channel(String channelName) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.messages = new ArrayList<>();
        this.userIds = new ArrayList<>(); //+
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

    public List<UUID> getUserIds() {
        return new ArrayList<>(userIds);
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

    public void addUser(UUID userId) {
        if (!userIds.contains(userId)) {
            userIds.add(userId);
            this.updatedAt = System.currentTimeMillis();
        }
    }

    public boolean isUserInChannel(UUID userId) {
        return userIds.contains(userId);
    }
}
