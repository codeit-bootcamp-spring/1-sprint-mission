package com.sprint.mission.discodeit.entity.data;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent {

    private final UUID id;
    private final Instant createdAt;
    private final byte[] data;
    private final UUID targetUUID;
    private final String fileName;
    private final String fileType;

    // 레포지토리에 저장시 음 User 프로필인지,Message 첨부파일지 구분해서 저장하게..?

    public BinaryContent(String filename,String fileType, UUID targetUUID, byte[] data){
        this.targetUUID = targetUUID;
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.fileType =fileType;
        this.fileName = filename;
        this.data = data;
    }


}
