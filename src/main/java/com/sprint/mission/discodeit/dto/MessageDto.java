package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(UUID id, UUID senderId, UUID channelId,Instant createdAt, Instant updatedAt, String content, List<MultipartFile> multipartFiles) {
    public MessageDto (UUID senderId, UUID channelId, String content){
        this(null, senderId, channelId, null, null,content, null);
    }
    public MessageDto (UUID id, String content){
        this(id, null, null, null, null,content, null);
    }
}
