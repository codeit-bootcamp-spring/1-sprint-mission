package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.enums.ContentType;
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

    public BinaryContent(BinaryContentDTO.request contentDto) {
        this.id = UUID.randomUUID();
        this.data = contentDto.file();
        this.referenceId = contentDto.referenceId();
        this.contentType = contentDto.contentType();
        this.mimeType = contentDto.mimeType();
        this.filename = contentDto.filename();
        this.createdAt = Instant.now();
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
