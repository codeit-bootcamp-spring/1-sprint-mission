package com.sprint.mission.discodeit.dto.binaryContent;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record BinaryContentDTO (UUID useerId, UUID messageId, MultipartFile multipartFile) {
}
