package com.spirnt.mission.discodeit.enity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

// 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private String fileName;    // 파일 원본 이름
    private String fileType;    // 파일 타입
    private String filePath;    // 서버에 저장된 경로

    public BinaryContent(UUID id, String fileName, String fileType, String filePath) {
        this.id = id;
        this.createdAt = Instant.now();
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "BinaryContent[ID:" + this.getId() +
                " FilePath: " + filePath + "]";
    }
}
