package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;
    private String channelName;
    private final List<UUID> messageList;

    public Channel(String channelName){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = createdAt;
        this.channelName = channelName;
        this.messageList = new ArrayList<>();
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = Instant.now().toEpochMilli();
    }

    public UUID addMessageToChannel(UUID messageUUID) {
        messageList.add(messageUUID);
        return messageUUID;
    }

    public String toString(){
        return "\nuuid: "+ id + " channelName: " + channelName;
    }
}
