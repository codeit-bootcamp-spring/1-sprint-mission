package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(UUID id, Instant createdAt, UUID domainId, MultipartFile multipartFile) {
    public BinaryContentDto(UUID domainId, MultipartFile  multipartFile) {
        this(null, null, domainId, multipartFile);
    }
    public BinaryContentDto(BinaryContent binaryContent){
        this(binaryContent.getId(), binaryContent.getCreatedAt(), binaryContent.getDomainId(), binaryContent.getMultipartFile());
    }
    public BinaryContentDto(MultipartFile  multipartFile) {
        this(null, null, null, multipartFile);
    }

    public BinaryContentDto(UUID domainId) {
        this(domainId, null, null, null);
    }
}
