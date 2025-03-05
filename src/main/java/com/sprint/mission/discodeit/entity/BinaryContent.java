package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

/**
 * - 이미지, 파일 등 바이너리 데이터를 표현
 * - 사용자의 프로필 이미지/ 메시지 첨부 파일 저장을 위해 사용
 */
@Getter
public class BinaryContent implements Serializable {
    private UUID id;
    private byte[] data;
    private UUID referenceId;
    private String mimeType;
    private String filename;
    private ContentType contentType;
    private Instant createdAt;

    public BinaryContent(UUID referenceId, byte[] fileData, ContentType contentType, String mimeType, String filename) {
        this.id = UUID.randomUUID();
        this.data = fileData;
        this.referenceId = referenceId;
        this.contentType = contentType;
        this.mimeType = mimeType;
        this.filename = filename;
        this.createdAt = Instant.now();
    }

    public enum ContentType {
        PROFILE,
        PICTURE
    }

    @Override
    public String toString() {
        return "BinaryContent{" +
                "id=" + id +
                ", data=" + Arrays.toString(data) +
                ", referenceId=" + referenceId +
                ", mimeType='" + mimeType + '\'' +
                ", filename='" + filename + '\'' +
                ", contentType=" + contentType +
                ", createdAt=" + createdAt +
                '}';
    }
}
