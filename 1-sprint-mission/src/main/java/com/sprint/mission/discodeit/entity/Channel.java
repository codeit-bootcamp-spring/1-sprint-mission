package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    //
    private UUID id;
    private User owner;
    //
    private String name; // public 채널 용
    private String description; // public 채널 용
    //
    private ChannelType type;

    private final Instant createdAt = Instant.now();
    private Instant updatedAt;

    private Instant lastMessageAt = Instant.EPOCH;

    public Channel(User owner, ChannelType type) {
        if(type != ChannelType.PRIVATE){
            throw new IllegalArgumentException("Invalid channel type");
        }
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.type = type;
        this.updatedAt = this.createdAt;
    }



    public Channel(User owner, String name, String description, ChannelType type) {
        if(type != ChannelType.PUBLIC){
            throw new IllegalArgumentException("Invalid channel type");
        }
        this.id = UUID.randomUUID();
        this.owner = owner;
        this.type = type;
        this.name = name;
        this.description = description;
        this.updatedAt = this.createdAt;
    }


    public void update(String newName, String newDescription) {
        boolean anyValueUpdated = false;
        if (newName != null && !newName.equals(this.name)) {
            this.name = newName;
            anyValueUpdated = true;
        }
        if (newDescription != null && !newDescription.equals(this.description)) {
            this.description = newDescription;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
