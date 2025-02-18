package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private String content;
    //
    private UUID channelId;
    private UUID authorId;

    @OneToMany(mappedBy = "messageId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BinaryContent> attachedFiles;

    public Message(String content, UUID channelId, UUID authorId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        //
        this.content = content;
        this.channelId = channelId;
        this.authorId = authorId;
    }

    public void update(String newContent) {
        boolean anyValueUpdated = false;
        if (newContent != null && !newContent.equals(this.content)) {
            this.content = newContent;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }
}
