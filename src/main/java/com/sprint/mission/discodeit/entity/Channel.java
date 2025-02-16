package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private String channelName;
    private String description;
    private ChannelType type;

    public Channel(String channelName, String description, ChannelType type) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.description = description;
        this.type = type;
        this.createdAt = Instant.now();
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
        this.updatedAt = Instant.now();
    }
    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public String toString() {
        return "Channel {" +
                "id = " + id +
                ", channelName = " + channelName +
                ", description = " + description +
                ", createdAt = " + createdAt +
                ", updatedAt = " + updatedAt + "}";
    }

}