package com.sprint.mission.discodeit.entity.binarycontent;

import com.sprint.mission.discodeit.util.time.TimeProvider;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 17L;
    private transient TimeProvider timeProvider;

    private final UUID id;

    private final String fileName;

    private String filePath; // 저장 경로
//    private final byte[] data;
    private final Long fileSize;

    private final String mimeType; //MIME 타입
    private final BinaryContentType type; //PROFILE, MESSAGE 유형

    private final Instant createdAt;

    public BinaryContent(String fileName, String filePath, Long fileSize, String mimeType, BinaryContentType type) {
        this.id = UUID.randomUUID();
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.type = type;
        createdAt = timeProvider.getCurrentTime();
    }

    /*
    UUID id;
    String fileName;
    String filePath;
    Long fileSize;
    String mimeType;
    BinaryContentType type;
    Instant createAt;
     */
}
