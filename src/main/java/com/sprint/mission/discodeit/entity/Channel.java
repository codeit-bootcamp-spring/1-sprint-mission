package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private final List<UUID> messageList;
    private final List<UUID> userList;
    private final boolean isPrivate;
    private final Map<UUID, ReadStatus> readStatuses;
    private final Instant lastMessageTime;

    public Channel(String channelName){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.messageList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.isPrivate = false;
        this.readStatuses = new HashMap<>();
        this.lastMessageTime = Instant.now();
    }

    public Channel(boolean isPrivate){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = createdAt;
        this.channelName = "";
        this.messageList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.isPrivate = isPrivate;
        this.readStatuses = new HashMap<>();
        this.lastMessageTime = Instant.now();
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = Instant.now();
    }

    public UUID addMessageToChannel(UUID messageUUID) {
        messageList.add(messageUUID);
        return messageUUID;
    }

    public UUID addUserToChannel(UUID userUUID) {
        userList.add(userUUID);
        return userUUID;
    }

    public void setReadStatus(ReadStatus readStatus) {
        this.readStatuses.put(readStatus.getOwnerId(), readStatus);
    }

    public void setReadStatuses(List<ReadStatus> readStatus) {
        for(ReadStatus status : readStatus){
            setReadStatus(status);
        }
    }

    public String toString(){
        return "\nuuid: "+ id + " channelName: " + channelName;
    }
}
