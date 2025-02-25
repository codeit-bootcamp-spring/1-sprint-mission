package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String filename;
    private Instant createdAt;
    private byte[] binaryImage;
    private String contentType;

    public BinaryContent(String filename, byte[] binaryImage , String contentType ) {
        this.id = UUID.randomUUID().toString();
        this.filename = filename;
        this.createdAt = Instant.now();
        this.binaryImage = binaryImage;
        this.contentType = contentType;
    }

}
