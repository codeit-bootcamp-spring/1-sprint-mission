package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
public class BinaryContent {

    @Id
    private UUID id;
    private UUID userId;
    private UUID messageId;
    private byte[] content;
    private String fileName;
    private String fileType;
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "message_id", insertable = false, updatable = false)
    private Message message;
}
