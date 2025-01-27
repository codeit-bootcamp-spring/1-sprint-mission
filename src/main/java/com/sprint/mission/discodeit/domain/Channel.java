package com.sprint.mission.discodeit.domain;

import com.sprint.mission.discodeit.util.ChannelType;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private UUID id;
    private Long createdAt;
    private Long updatedAt;
    private String name;
    private String description;
    private ChannelType channelType;

    public Channel(String name, String description, ChannelType channelType) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = null;
        this.name = name;
        this.description = description;
        this.channelType = channelType;
    }

    public void changeType(ChannelType channelType) {
        this.channelType = channelType;
        this.updatedAt = System.currentTimeMillis();
    }

    public void changeDescription(String description) {
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String createdAtFormatted = createdAt != null
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault()).format(formatter)
                : "N/A";
        String updatedAtFormatted = updatedAt != null
                ? LocalDateTime.ofInstant(Instant.ofEpochMilli(updatedAt), ZoneId.systemDefault()).format(formatter)
                : "N/A";

        return "Channel{" +
                "id=" + id +
                ", createdAt=" + createdAtFormatted +
                ", updatedAt=" + updatedAtFormatted +
                ", description=" + description +
                ", name='" + name + '\'' +
                ", type='" + channelType + '\'' +
                '}';
    }

}
