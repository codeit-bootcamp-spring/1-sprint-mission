package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

// BinaryContent: 이미지 및 파일 저장 (수정 불가)
public class BinaryContent {
    private UUID id;
    private Instant createdAt;
    private UUID ownerId; // User or Message owner
    private byte[] content;
}
