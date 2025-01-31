package com.sprint.mission.discodeit.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String channelName;
    private List<UUID> messageList;

    public UUID getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public String getChannelName() {
        return channelName;
    }

    public List<UUID> getMessageList() {
        return messageList;
    }

    public UUID addMessageToChannel(UUID messageUUID) {
        messageList.add(messageUUID);
        return messageUUID;
    }

    public String toString(){
        return "\nuuid: "+ id + " channelName: " + channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    }

    public Channel(String channelName){
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.messageList = new ArrayList<>();
    }
}
