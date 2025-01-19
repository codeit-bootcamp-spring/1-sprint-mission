package com.sprint.mission.discodeit.entity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Channel {
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    private String channelName;
    private Map<UUID, Message> messages;

    public Map<UUID, Message> getMessages() {
        return messages;
    }

    public Message addMessage(Message message) {
        if (messages == null) {
            messages = new HashMap<>();
        } else {
            messages.put(message.getId(), message);
        }
        return message;
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

    public String getChannelName() {
        return channelName;
    }

    public String toString(){
        return "\nuuid: "+ id + " channelName: " + channelName + " messages: " + messages.toString();
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
        this.messages = new HashMap<>();
    }
}
