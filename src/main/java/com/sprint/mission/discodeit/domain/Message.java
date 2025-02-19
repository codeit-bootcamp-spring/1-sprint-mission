package com.sprint.mission.discodeit.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Getter
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
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
        return "Message{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", content='" + content + '\'' +
                ", writer='" + writerID + '\'' +
                ", channel='" + channelID + '\'' +
                '}';
    }
}
