package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String title;
    private String description;
    private ChannelType channelType;

    public enum ChannelType {
        PUBLIC,
        PRIVATE
    }

    public static Channel createChannel(ChannelType channelType, String title, String description) {
        return new Channel(channelType, title, description);
    }

    private Channel(ChannelType channelType, String title, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.title = title;
        this.description = description;
        this.channelType = channelType;
    }

    public void update(String newTitle, String newDescription) {
        boolean isChanged = false;
        if (!newTitle.equals(this.title)) {
            this.title = newTitle;
            isChanged = true;
        }
        if (!newDescription.equals(this.description)) {
            this.description = newDescription;
            isChanged = true;
        }
        if (isChanged) {
            this.updatedAt = Instant.now();
        }
    }

    @Override
    public String toString() {
        return "Channel{id:" + id + ",title:" + title +  ",createdAt:" + createdAt + ",updatedAt:" + updatedAt + "}";
    }

}
