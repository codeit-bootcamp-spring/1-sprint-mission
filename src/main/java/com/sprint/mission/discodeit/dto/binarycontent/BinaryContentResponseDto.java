package com.sprint.mission.discodeit.dto.binarycontent;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BinaryContentResponseDto(UUID id, String fileName, Long size, String contentType, byte[] bytes) {
}
