package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String name;
    private String description;
    private ChannelType channelType;
    private List<UUID> participants = new LinkedList<>();

    public Channel() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    public Channel(String name, String description, ChannelType channelType) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.name = name;
        this.description = description;
        this.channelType = channelType;
    }

    public void update(String name, String description, ChannelType channelType, List<UUID> participants) {
        this.name = name;
        this.description = description;
        this.channelType = channelType;
        updatedAt = Instant.now();
    }

    public void addParticipant(UUID userId){
        participants.add(userId);
    }

    public void removeParticipant(UUID userId){
        participants.remove(userId);
    }
}
