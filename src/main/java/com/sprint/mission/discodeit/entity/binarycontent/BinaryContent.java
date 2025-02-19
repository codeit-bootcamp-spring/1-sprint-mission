package com.sprint.mission.discodeit.entity.binarycontent;

import com.sprint.mission.discodeit.entity.ImmutableBaseObject;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class BinaryContent extends ImmutableBaseObject {
    private static final String TYPE_IMG = "image";
    private static final String TYPE_BIN = "binary";

    @NotEmpty
    private final UUID userId;

    @NotEmpty
    private final UUID messageId;

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String fileType;

    @NotEmpty
    private String filePath = "~/Desktop/";

    private long fileSize;

    public BinaryContent(UUID userId, UUID messageId, String fileName, String fileType) {
        super();
        this.userId = userId;
        this.messageId = messageId;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public BinaryContent(UUID userId, String fileName, String fileType) {
        this.userId = userId;
        this.messageId = UUID.randomUUID();
        this.fileName = fileName;
        this.fileType = fileType;
    }



    public BinaryContent upload(String fileName, String fileType) {
        this.fileName = fileName;
        this.fileType = fileType;

        return this;
    }

    /**
     * @param fileSize
     * @Description: 임시로 파일 크기 지정
     */
    public void calculateFileSize(long fileSize){
        this.fileSize = fileSize;
    }
}
