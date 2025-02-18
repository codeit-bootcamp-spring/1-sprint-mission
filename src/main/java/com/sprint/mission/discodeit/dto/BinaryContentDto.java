package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.io.File;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(UUID id, Instant createdAt, UUID domainId, File file) {
    public BinaryContentDto(UUID domainId, File file) {
        this(null, null, domainId, file);
    }
    public BinaryContentDto(BinaryContent binaryContent){
        this(binaryContent.getId(), binaryContent.getCreatedAt(), binaryContent.getDomainId(), binaryContent.getFile());
    }
    public BinaryContentDto(File file) {
        this(null, null, null, file);
    }
}
