package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile multipartFile;

    public BinaryContent(UUID domainId, MultipartFile multipartFile) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.domainId = domainId;
        this.multipartFile = multipartFile;
    }
    public BinaryContent update(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        return this;
    }
}
