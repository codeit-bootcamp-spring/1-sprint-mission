package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class Channel implements Serializable {
    private final String id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType type;

    public Channel(String name, String description, ChannelType channelType) {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        this.name = name;
        this.description = description;
        this.type = channelType;
    }

    public void update(String name, String description, ChannelType channelType) {
        this.name = name != null ? name : this.name;
        this.description = description != null ? description : this.description;
        this.updatedAt = Instant.now();
        this.type = channelType != null ? channelType : this.type;
    }
}