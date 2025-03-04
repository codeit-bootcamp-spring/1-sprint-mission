package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final Instant createdAt;
    private String fileNm;
    private Long size;
    private String contentType;
    private byte[] content;


    public BinaryContent(String fileNm, Long size, String contentType, byte[] content) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileNm = fileNm;
        this.size = size;
        this.contentType = contentType;
        this.content = content;
    }

}
