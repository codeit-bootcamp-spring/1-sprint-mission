package com.sprint.mission.discodeit.dto.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent {
    private UUID id;
    private Instant createAt;

    private String uploadFileName;
    private Long size;
    private String contentType;
    private byte[] bytes;

    public BinaryContent(String uploadFileName, Long size, String contentType, byte[] bytes) {
        this.uploadFileName = uploadFileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
