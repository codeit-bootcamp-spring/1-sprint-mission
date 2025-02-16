package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
@Entity
@RequiredArgsConstructor
@Data
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private Instant createdAt;
    private UUID domainId; //UserProfile, Message's file
    private File file;

    public BinaryContent(UUID domainId, File file) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.domainId = domainId;
        this.file = file;
    }
    public BinaryContent update(File file) {
        this.file = file;
        return this;
    }
}
