package com.sprint.mission.entity.main;

import com.sprint.mission.dto.request.ChannelDtoForRequest;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

@Getter
@Setter
public class Channel implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private ChannelType channelType;
    private String name;
    private String description;

    public Channel(String name, String description, ChannelType channelType) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.channelType = channelType;
        this.description = description;
        this.createdAt = Instant.now();
    }

    public void updateByDTO(ChannelDtoForRequest dto){
        this.name = dto.newName();
        this.description = dto.newDescription();
        this.updatedAt = Instant.now();
    }
}