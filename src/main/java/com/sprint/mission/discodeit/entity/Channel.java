package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;
    private final UUID id;
    private String channelName;
    private String description;
    private final Instant createdAt;
    private Instant updatedAt;

    public Channel(String channelName, String description) {
        this.id = UUID.randomUUID();
        this.channelName = channelName;
        this.description = description;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }


    public void updateChannelName(String newChannelName) {
        this.channelName = newChannelName;
        this.updatedAt = Instant.now();
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = Instant.now();
    }

}
