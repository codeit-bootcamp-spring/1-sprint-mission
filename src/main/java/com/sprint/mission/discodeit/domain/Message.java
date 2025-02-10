package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Message implements Serializable {
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    private String content;
    private UUID writerID;
    private UUID channelID;
    private List<UUID> attachmentsID;

    public Message(String content, UUID writerID, UUID channelID, List<UUID> attachmentsID) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.content = content;
        this.writerID = writerID;
        this.channelID = channelID;
        this.attachmentsID = attachmentsID;
    }

    public void update(String content, UUID newAttachment) {
        this.content = content;
        this.attachmentsID.add(newAttachment);
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        String writerUserName = writer != null ? writer.getName() : "Unknown";
        String useChannelName = channel != null ? channel.getName() : "Unknown";

        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", writer='" + writerUserName + '\'' +
                ", channel='" + useChannelName + '\'' +
                '}';
    }
}
