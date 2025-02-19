package com.spirnt.mission.discodeit.enity;

import com.spirnt.mission.discodeit.dto.binaryContent.BinaryContentCreate;
import lombok.Getter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

// 이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델
@Getter
public class BinaryContent implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private Instant createdAt;
    private UUID userId;    // 업로드한 유저
    private UUID messageId; //첨부된 메세지
    private String fileName;    // 파일 원본 이름
    private String fileType;    // 파일 타입
    private String filePath;    // 서버에 저장된 경로

    public BinaryContent(UUID id, UUID userId, UUID messageId, String fileName, String fileType, String filePath) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
        this.messageId = messageId;
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
