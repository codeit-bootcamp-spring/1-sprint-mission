package com.sprint.mission.discodeit.dto.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private UUID id;
    private Instant createAt;

    private String uploadFileName;
    private Long size;
    private String contentType;
    private byte[] bytes;

    public BinaryContent(String uploadFileName, Long size, String contentType, byte[] bytes) {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();

        this.uploadFileName = uploadFileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
