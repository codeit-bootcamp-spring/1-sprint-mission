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

    private byte[] data;  // 바이너리 데이터 저장
    private String contentType;
    private Long size;
    private String fileName;


    public BinaryContent(byte[] data, String fileName, String contentType, Long size) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.data = data;
        this.contentType = contentType;
        this.size =size;
    }

}
