package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.status.BinaryContentUploadStatus;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentDto(
    UUID id,
    String fileName,
    String contentType,
    Instant createdAt,
    Long size,
    BinaryContentUploadStatus uploadStatus
) {

}
