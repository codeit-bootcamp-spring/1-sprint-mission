package com.sprint.mission.discodeit.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.io.File;
import java.time.Instant;
import java.util.UUID;
@Entity
@RequiredArgsConstructor
@Data
public class BinaryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private Instant createdAt;
    private UUID domainId; //UserProfile, Message's file
    private File file;

    public BinaryContent(UUID domainId, File file) {
        this.createdAt = Instant.now();
        this.domainId = domainId;
        this.file = file;
    }
}
