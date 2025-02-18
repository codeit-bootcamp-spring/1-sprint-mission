package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent {
    private UUID id;
    private Instant createdAt;
    // 파일 이름
    private String fileName;
    // 파일 사이즈
    private Long size;
    // 프로필이미지, 메시지 파일인지 구분.
    private String contentType;
    // 파일 바이너리 데이터
    private byte[] bytes;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}
